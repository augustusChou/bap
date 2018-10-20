package com.github.bap.event.source;

/**
 * @author 周广
 **/
public interface EventHandler<T> {

    /**
     * 事件处理方法
     *
     * @param event mysql事件
     */
    void handler(Event<T> event);


}
