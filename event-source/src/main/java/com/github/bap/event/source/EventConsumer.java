package com.github.bap.event.source;

/**
 * @author 周广
 **/
public interface EventConsumer<T> {

    /**
     * 获取消费者id
     *
     * @return 消费者id
     */
    String getConsumerId();

    /**
     * 消费事件
     *
     * @param event 事件
     */
    void consumer(Event<T> event);

    /**
     * 过滤事件、判断是否为自己所需要
     *
     * @param event 事件
     * @return 返回是否为可用事件
     */
    boolean filter(Event<T> event);

}
