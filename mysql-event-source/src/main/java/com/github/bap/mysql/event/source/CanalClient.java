package com.github.bap.mysql.event.source;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.github.bap.event.source.EventConsumer;
import com.github.bap.event.source.EventProducer;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author 周广
 **/
@Slf4j
public class CanalClient {


    private CanalConnector canalConnector;
    private EventProducer<CanalEventInfo> eventProducer;
    private boolean running;
    private int sleepMillis = 1000;
    private ExecutorService executor;
    private CanalMessageParse<CanalEventInfo> messageParse;

    /**
     * 批数据大小
     */
    private int batchSize = 100;

    public CanalClient() {
        this(AddressUtils.getHostIp(), 11111, "example");
    }

    public CanalClient(String ip, Integer port) {
        this(ip, port, "example");
    }

    public CanalClient(String canalServerIp, int canalServerPort, String destination) {
        this(canalServerIp, canalServerPort, destination, "", "");
    }

    public CanalClient(String canalServerIp, int canalServerPort, String destination, String username, String password) {
        this.eventProducer = new MysqlEventProducer();
        this.canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalServerIp, canalServerPort),
                destination,
                username,
                password);
        this.messageParse = new EventMessageParse();
    }

    public CanalClient(CanalConnector canalConnector, CanalMessageParse<CanalEventInfo> messageParse) {
        this.eventProducer = new MysqlEventProducer();
        this.canalConnector = canalConnector;
        this.messageParse = messageParse;
    }

    /**
     * 开始监听处理mysql事件
     */
    public synchronized void start() throws CanalClientException {
        if (running) {
            return;
        }
        executor = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("CanalClient-Single-Thread-Pool"));

        try {
            this.canalConnector.connect();
            this.canalConnector.subscribe(".*\\..*");
            this.canalConnector.rollback();
        } catch (CanalClientException e) {
            this.canalConnector.disconnect();
            throw e;
        }

        this.running = true;
        executor.execute(() -> {
            while (running) {
                Message message = canalConnector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    log.info("获取数据，大小:{}", size);
                    this.messageParse.convertEvent(message).forEach(event -> {
                        eventProducer.filterNotifyConsumer(event);
                    });
                }
                // 提交确认
                canalConnector.ack(batchId);
            }
        });

        log.info("开始执行Canal监听处理");
    }


    /**
     * 停止执行事件监听
     */
    public void stop() {
        if (this.running) {
            this.running = false;
            this.executor.shutdown();
            this.canalConnector.stopRunning();
            log.info("停止执行Canal事件监听");
        }
    }

    public boolean isRunning() {
        return running;
    }


    /**
     * 注册事件消费者
     *
     * @param eventConsumer 事件消费者
     */
    public void registerConsumer(EventConsumer<CanalEventInfo> eventConsumer) {
        this.eventProducer.registerConsumer(eventConsumer);
    }

    /**
     * 移除事件消费者
     *
     * @param consumerId 消费者id
     */
    public void removeConsumer(String consumerId) {
        this.eventProducer.removeConsumer(consumerId);
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getSleepMillis() {
        return sleepMillis;
    }

    public void setSleepMillis(int sleepMillis) {
        this.sleepMillis = sleepMillis;
    }


}
