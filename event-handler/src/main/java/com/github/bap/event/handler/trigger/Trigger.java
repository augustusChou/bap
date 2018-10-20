package com.github.bap.event.handler.trigger;

import java.util.List;

/**
 * 触发器接口
 *
 * @author 周广
 **/
public interface Trigger<T> {

    /**
     * 获取触发条件
     *
     * @return 触发条件
     */
    Condition<T> getTriggerCondition();

    /**
     * 添加在触发后执行的操作
     *
     * @param operation 触发器被触发后执行的操作
     */
    void addTriggerOperation(Operation<T> operation);


    /**
     * 获取触发器操作列表
     *
     * @return 操作列表
     */
    List<Operation<T>> getOperation();
}
