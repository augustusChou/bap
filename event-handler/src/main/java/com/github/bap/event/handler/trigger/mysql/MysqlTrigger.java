package com.github.bap.event.handler.trigger.mysql;

import com.github.bap.event.handler.trigger.AbstractTrigger;
import com.github.bap.event.handler.trigger.Condition;
import com.github.bap.event.handler.trigger.Operation;
import com.github.bap.mysql.event.source.CanalEventInfo;

import java.util.List;

/**
 * mysql事件触发器类
 *
 * @author 周广
 **/
public class MysqlTrigger extends AbstractTrigger<CanalEventInfo> {

    private Condition<CanalEventInfo> condition;

    public MysqlTrigger(Condition<CanalEventInfo> condition) {
        this.condition = condition;
    }

    public MysqlTrigger(List<Operation<CanalEventInfo>> operations, Condition<CanalEventInfo> condition) {
        super(operations);
        this.condition = condition;
    }

    @Override
    public Condition<CanalEventInfo> getTriggerCondition() {
        return this.condition;
    }



}
