package com.github.bap.mysql.event.source;

import com.github.bap.event.source.AbstractEvent;

/**
 * @author 周广
 **/
public class MysqlEvent extends AbstractEvent<CanalEventInfo> {

    public MysqlEvent(CanalEventInfo data) {
        super(data, EventType.MYSQL);
    }


}
