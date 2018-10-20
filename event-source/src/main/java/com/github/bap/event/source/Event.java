package com.github.bap.event.source;

/**
 * @author 周广
 **/
public interface Event<T> {

    /**
     * 获取事件唯一id (uuid)
     *
     * @return 事件id
     */
    String getEventId();

    /**
     * 获取事件包装的数据信息
     * @return 事件包装的数据信息
     */
    T getData();

}
