package com.github.bap.event.handler.trigger;

import com.github.bap.event.source.Event;

/**
 * @author 周广
 **/
public interface Condition<T> {

    /**
     * 条件匹配
     *
     * @param event 时间
     * @return 是否是自己关注的事件消息
     */
    boolean match(Event<T> event);

}
