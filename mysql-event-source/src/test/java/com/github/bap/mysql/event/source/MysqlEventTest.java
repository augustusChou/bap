package com.github.bap.mysql.event.source;

import com.github.bap.event.source.AbstractEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author 周广
 **/
public class MysqlEventTest {


    @Test
    public void mysqlEventTest() {
        CanalEventInfo eventInfo = new CanalEventInfo();
        MysqlEvent mysqlEvent = new MysqlEvent(eventInfo);

        Assert.assertEquals(eventInfo, mysqlEvent.getData());
        Assert.assertNotNull(mysqlEvent.getEventId());
        Assert.assertEquals(mysqlEvent.getEventType(), AbstractEvent.EventType.MYSQL);
    }
}