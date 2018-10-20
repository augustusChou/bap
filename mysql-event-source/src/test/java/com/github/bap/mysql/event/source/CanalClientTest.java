package com.github.bap.mysql.event.source;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.google.common.collect.Lists;
import com.github.bap.event.source.Event;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author 周广
 **/
public class CanalClientTest {

    /**
     * 没有数据的客户端对象
     */
    private CanalClient notMessageClient;
    /**
     * 有数据的客户端对象
     */
    private CanalClient haveMessageClient;

    /**
     * 会抛出异常的client
     */
    private CanalClient exceptionMessageClient;

    @Before
    public void setUp() throws Exception {
        MysqlEventConsumer consumer = new MysqlEventConsumer("id", event -> {
            Assert.assertEquals(event.getData().getEventType(), MysqlEventType.DELETE);
        }, event -> true);


        /*
           mock一个不包含消息数据的message对象
         */
        Message notMessageBean = mock(Message.class);
        when(notMessageBean.getId()).thenReturn(-1L);
        when(notMessageBean.getEntries()).thenReturn(Collections.emptyList());

        CanalConnector notDataCanalConnector = mock(CanalConnector.class);
        when(notDataCanalConnector.getWithoutAck(anyInt())).thenReturn(notMessageBean);

        notMessageClient = new CanalClient(notDataCanalConnector, new EventMessageParse());
        notMessageClient.registerConsumer(consumer);




        /*
          mock一个有1条记录数据的message对象
         */
        Message haveMessageBean = mock(Message.class);
        when(haveMessageBean.getId()).thenReturn(1L);
        List list = Lists.newArrayList();
        list.add(new Object());
        when(haveMessageBean.getEntries()).thenReturn(list);

        CanalConnector haveDataCanalConnector = mock(CanalConnector.class);
        when(haveDataCanalConnector.getWithoutAck(anyInt())).thenReturn(haveMessageBean);

        List<Event<CanalEventInfo>> eventList = Lists.newArrayList();
        CanalEventInfo eventInfo = new CanalEventInfo();
        eventInfo.setEventType(MysqlEventType.DELETE);
        eventList.add(new MysqlEvent(eventInfo));
        CanalMessageParse<CanalEventInfo> messageParse = mock(EventMessageParse.class);
        when(messageParse.convertEvent(any(Message.class))).thenReturn(eventList);

        haveMessageClient = new CanalClient(haveDataCanalConnector, messageParse);
        haveMessageClient.registerConsumer(consumer);


        CanalConnector exceptionCanalConnector = mock(CanalConnector.class);
        doThrow(CanalClientException.class).when(exceptionCanalConnector).connect();
        exceptionMessageClient = new CanalClient(exceptionCanalConnector, null);

    }

    @Test(expected = CanalClientException.class)
    public void throwExceptionTest() {
        exceptionMessageClient.start();
    }

    @org.junit.Test
    public void start() throws InterruptedException {
        notMessageClient.start();
        haveMessageClient.start();

        //加一点睡眠时间，让有消息的messageClient可以去消费message
        Thread.sleep(10);

        if (notMessageClient.isRunning()) {
            notMessageClient.stop();
        }
        if (haveMessageClient.isRunning()) {
            haveMessageClient.stop();
        }

        Assert.assertFalse(notMessageClient.isRunning());
        Assert.assertFalse(haveMessageClient.isRunning());
    }


}