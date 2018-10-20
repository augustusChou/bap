package com.github.bap.event.handler.core;

import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.domain.EventRuleDomain;
import com.github.bap.event.handler.dao.po.DbEventRulePO;
import com.github.bap.event.handler.trigger.Operation;
import com.github.bap.event.handler.trigger.Trigger;
import com.github.bap.event.handler.dao.repository.EventRuleRepository;
import com.github.bap.event.source.EventConsumer;
import com.github.bap.mysql.event.source.CanalClient;
import com.github.bap.mysql.event.source.CanalEventInfo;
import com.github.bap.mysql.event.source.MysqlEventConsumer;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 周广
 **/
@Component
@Slf4j
public class MysqlEventHandlerInitialize implements InitializingBean, DisposableBean {


    /**
     * Canal客户端对象，mysql事件处理核心类
     */
    private CanalClient canalClient;
    /**
     * 用于在异常状态下启动CanalClient的线程池
     */
    private ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(
            1, new DefaultThreadFactory("CanalClient-Start-Thread"));
    /**
     * 执行脚本的线程池大小
     */
    private static final int POOL_SIZE = 16;
    /**
     * 执行脚本的线程池
     */
    private ScheduledExecutorService operationExecPool = new ScheduledThreadPoolExecutor(
            POOL_SIZE, new DefaultThreadFactory("Script-Exec-ThreadPool"));
    @Autowired
    private EventRuleRepository eventRuleRepository;
    @Autowired
    private DomainFactory domainFactory;

    @Value("${canal.ip}")
    private String canalIp;
    @Value("${canal.port}")
    private Integer canalPort;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.canalClient = new CanalClient(canalIp, canalPort);
        this.registerConsumer();
        startCanalClient();
    }

    @Override
    public void destroy() throws Exception {
        if (this.canalClient != null) {
            this.canalClient.stop();
        }
    }

    /**
     * 更新消费者
     *
     * @param rulePO 消费者匹配规则信息
     */
    public void replaceConsumer(DbEventRulePO rulePO) {
        registerOrReplaceConsumer(rulePO);
    }


    private void startCanalClient() {
        try {
            canalClient.start();
            timer.shutdown();
        } catch (Exception e) {
            log.warn("启动canal客户端失败", e);
            timer.schedule(this::startCanalClient, 10, TimeUnit.SECONDS);
        }
    }

    /**
     * 加载数据库中存储的触发器 并注册到Canal Client消费者
     */
    private void registerConsumer() {
        for (DbEventRulePO rulePO : eventRuleRepository.findAllByRuleStatus(EventRuleDomain.RULE_ON_STATUS)) {
            registerOrReplaceConsumer(rulePO);
        }
    }


    public void registerOrReplaceConsumer(EventRuleDomain eventRuleDomain) {
        Trigger<CanalEventInfo> trigger = eventRuleDomain.createNewTrigger(this.operationExecPool);

        EventConsumer<CanalEventInfo> eventConsumer = new MysqlEventConsumer(
                String.valueOf(eventRuleDomain.getRuleId()),
                event -> {
                    for (Operation<CanalEventInfo> operation : trigger.getOperation()) {
                        try {
                            operation.executor(event);
                        } catch (Exception e) {
                            log.error("执行触发器操作失败", e);
                        }
                    }
                },
                event -> trigger.getTriggerCondition().match(event));
        this.canalClient.registerConsumer(eventConsumer);
    }

    private void registerOrReplaceConsumer(DbEventRulePO rulePO) {
        EventRuleDomain eventRuleDomain = domainFactory.createEventRuleDomain(rulePO);
        registerOrReplaceConsumer(eventRuleDomain);
    }

    /**
     * 移除事件消费者
     *
     * @param consumerId 消费者id
     */
    public void removeConsumer(String consumerId) {
        this.canalClient.removeConsumer(consumerId);
    }

}
