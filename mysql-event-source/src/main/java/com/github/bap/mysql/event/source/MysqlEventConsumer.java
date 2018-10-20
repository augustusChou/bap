package com.github.bap.mysql.event.source;

import com.github.bap.event.source.AbstractEventConsumer;
import com.github.bap.event.source.Event;
import com.github.bap.event.source.EventHandler;

/**
 * @author 周广
 **/
public class MysqlEventConsumer extends AbstractEventConsumer<CanalEventInfo> {

    private EventHandler<CanalEventInfo> eventHandler;
    private EventFilter<CanalEventInfo> eventFilter;

    public MysqlEventConsumer(String consumerId,
                              EventHandler<CanalEventInfo> eventHandler,
                              EventFilter<CanalEventInfo> eventFilter) {
        super(consumerId);
        this.eventHandler = eventHandler;
        this.eventFilter = eventFilter;
    }


    @Override
    public void consumer(Event<CanalEventInfo> event) {
        this.eventHandler.handler(event);
    }

    @Override
    public boolean filter(Event<CanalEventInfo> event) {
        return this.eventFilter.filter(event);
    }

}
