package com.github.bap.event.handler.script.func;

import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.po.DbEventFuncUseLogPO;
import com.github.bap.event.handler.dao.repository.EventFuncUseLogRepository;
import com.github.bap.event.handler.service.EnvService;
import com.github.bap.event.handler.script.OperationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

/**
 * @author 周广
 **/
@Component
public class FunctionBean {

    /**
     * 当前执行操作的操作id
     */
    public static final ThreadLocal<OperationContext> OPERATION_ID = new ThreadLocal<>();

    private static FunctionBean FUNCTION_BEAN;
    @Autowired
    private EnvService envService;
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private EventFuncUseLogRepository eventFuncUseLogRepository;

    public static FunctionBean getFunctionBean() {
        return FUNCTION_BEAN;
    }

    @PostConstruct
    public void init() {
        FUNCTION_BEAN = this;
    }

    public DomainFactory getDomainFactory() {
        return domainFactory;
    }

    /**
     * 记录java提供给js使用的功能的使用情况
     *
     * @param configId 配置id
     * @param info     记录的信息
     */
    public void logFunctionUse(Integer configId, String info) {
        DbEventFuncUseLogPO po = createUseLogPO(configId, info);
        eventFuncUseLogRepository.save(po);
    }

    /**
     * 获取环境枚举
     *
     * @return 环境枚举
     */
    public EnvService.EnvEnum getEnvEnum() {
        return envService.getEnv();
    }

    private DbEventFuncUseLogPO createUseLogPO(Integer configId, String info) {
        DbEventFuncUseLogPO po = new DbEventFuncUseLogPO();
        po.setOperationId(Integer.parseInt(OPERATION_ID.get().getOperationId()));
        po.setConfigId(configId);
        po.setConfigParam(info);
        po.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return po;
    }


}
