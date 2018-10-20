package com.github.bap.event.handler.trigger;

import com.github.bap.event.source.Event;

/**
 * @author 周广
 **/
public interface LoggingHandler<T> {


    /**
     * 记录 日志
     *
     * @param event   事件信息
     * @param logInfo 日志信息
     */
    void log(Event<T> event, Object logInfo);

}
