package com.github.bap.event.source;

/**
 * @author 周广
 **/
public interface EventProducer<T> {

    /**
     * 注册一个事件消费者
     *
     * @param consumer 事件消费者对象
     */
    void registerConsumer(EventConsumer<T> consumer);

    /**
     * 移除消费者
     *
     * @param consumerId 消费者id
     */
    void removeConsumer(String consumerId);


    /**
     * 通知指定的消费者新事件
     *
     * @param consumerId 消费者Id
     * @param event      事件
     */
    void notifyConsumer(String consumerId, Event<T> event);

    /**
     * 通知所有消费者新事件
     *
     * @param event 事件
     */
    void notifyAllConsumer(Event<T> event);

    /**
     * 只给消费方发送需要的事件
     *
     * @param event 事件
     */
    void filterNotifyConsumer(Event<T> event);

}
