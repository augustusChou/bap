package com.github.bap.event.handler.trigger;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 周广
 **/
public abstract class AbstractTrigger<T> implements Trigger<T> {

    /**
     * 触发后操作链表
     */
    protected List<Operation<T>> operationList;

    public AbstractTrigger() {
        this.operationList = new LinkedList<>();
    }

    public AbstractTrigger(List<Operation<T>> operationList) {
        this.operationList = operationList;
    }

    @Override
    public void addTriggerOperation(Operation<T> operation) {
        this.operationList.add(operation);
    }


    @Override
    public List<Operation<T>> getOperation() {
        return this.operationList;
    }
}
