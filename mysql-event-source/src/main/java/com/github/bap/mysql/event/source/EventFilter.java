package com.github.bap.mysql.event.source;

import com.github.bap.event.source.Event;

/**
 * @author 周广
 **/
public interface EventFilter<T> {

    /**
     * 判断过滤事件是否可用
     *
     * @param event 事件
     * @return 是否可用
     */
    boolean filter(Event<T> event);

}
