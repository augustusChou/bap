package com.github.bap.event.handler.trigger;

import com.github.bap.event.source.Event;

/**
 * @author 周广
 **/
public interface Operation<T> {

    /**
     * 获取操作id，用于区别不同的操作对象
     *
     * @return 操作id
     */
    String getOperationId();

    /**
     * 执行操作
     *
     * @param event 事件信息
     * @throws Exception 异常信息
     */
    void executor(Event<T> event) throws Exception;

    /**
     * 设置日志处理函数
     *
     * @param logHandler 日志处理函数
     */
    void setLogHandler(LoggingHandler<T> logHandler);
}
