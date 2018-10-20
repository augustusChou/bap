package com.github.bap.mysql.event.source;

import com.alibaba.otter.canal.protocol.Message;
import com.github.bap.event.source.Event;

import java.util.List;

/**
 * @author 周广
 **/
public interface CanalMessageParse<T> {

    /**
     * 解析canal消息对象，将其转化为内部事件
     *
     * @param message canal消息对象
     * @return 内部事件列表
     */
    List<Event<T>> convertEvent(Message message);
}
