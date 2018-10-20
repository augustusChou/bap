package com.github.bap.event.source;

/**
 * @author 周广
 **/
public abstract class AbstractEventConsumer<T> implements EventConsumer<T> {

    /**
     * 消费者id
     */
    private String consumeId;


    public AbstractEventConsumer(String consumeId) {
        this.consumeId = consumeId;
    }

    @Override
    public String getConsumerId() {
        return this.consumeId;
    }


    @Override
    public boolean filter(Event<T> event) {
        return true;
    }
}
