package com.github.bap.event.source;

import com.github.bap.common.UUIDUtil;

/**
 * @author 周广
 **/
public abstract class AbstractEvent<T> implements Event<T> {

    protected T data;
    private String eventId;
    private long createTimestamp;
    private EventType eventType;

    public AbstractEvent(T data,EventType eventType) {
        this.data = data;
        this.eventId = generateEventId();
        this.eventType=eventType;
        this.createTimestamp = System.currentTimeMillis();
    }

    private String generateEventId() {
        return UUIDUtil.getUUID();
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public T getData() {
        return data;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public enum EventType {
        /**
         * 数据库事件
         */
        MYSQL,
        /**
         * 消息事件
         */
        MQ;
    }
}
