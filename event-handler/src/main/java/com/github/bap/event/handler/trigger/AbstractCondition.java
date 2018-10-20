package com.github.bap.event.handler.trigger;

import com.github.bap.event.source.AbstractEvent;

/**
 * @author 周广
 **/
public abstract class AbstractCondition<T> implements Condition<T> {

    /**
     * 匹配的事件类型
     */
    protected AbstractEvent.EventType matchEventType;

    public AbstractCondition(AbstractEvent.EventType matchEventType) {
        this.matchEventType = matchEventType;
    }


}
