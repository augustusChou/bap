package com.github.bap.event.source;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周广
 **/
public abstract class AbstractEventProducer<T> implements EventProducer<T> {

    /**
     * 消费者map
     */
    protected Map<String, EventConsumer<T>> consumerMap = new ConcurrentHashMap<>();

    @Override
    public void registerConsumer(EventConsumer<T> consumer) {
        consumerMap.put(consumer.getConsumerId(), consumer);
    }

    @Override
    public void removeConsumer(String consumerId) {
        consumerMap.remove(consumerId);
    }

    @Override
    public void notifyConsumer(String consumerId, Event<T> event) {
        EventConsumer<T> consumer = consumerMap.get(consumerId);
        if (consumer == null) {
            throw new ConsumerNotRegisterException();
        }
        consumer.consumer(event);
    }

    @Override
    public void notifyAllConsumer(Event<T> event) {
        consumerMap.forEach((key, consumer) -> {
            consumer.consumer(event);
        });
    }
}
