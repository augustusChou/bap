package com.github.bap.mysql.event.source;

import com.github.bap.event.source.AbstractEventProducer;
import com.github.bap.event.source.Event;

/**
 * @author 周广
 **/
public class MysqlEventProducer extends AbstractEventProducer<CanalEventInfo> {


    @Override
    public void filterNotifyConsumer(Event<CanalEventInfo> event) {
        consumerMap.forEach((key, consume) -> {
            if (consume.filter(event)) {
                consume.consumer(event);
            }
        });
    }
}
