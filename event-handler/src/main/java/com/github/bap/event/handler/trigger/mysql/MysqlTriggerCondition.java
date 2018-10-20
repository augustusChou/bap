package com.github.bap.event.handler.trigger.mysql;

import com.github.bap.event.handler.trigger.AbstractCondition;
import com.github.bap.event.source.AbstractEvent;
import com.github.bap.event.source.Event;
import com.github.bap.mysql.event.source.CanalEventInfo;
import com.github.bap.mysql.event.source.MysqlEventType;

/**
 * mysql事件触发判断条件
 *
 * @author 周广
 **/
public class MysqlTriggerCondition extends AbstractCondition<CanalEventInfo> {

    /**
     * 数据库名称
     */
    private String schemaName;
    /**
     * 数据表名称
     */
    private String tableName;
    /**
     * insert/update/delete类型
     */
    private MysqlEventType eventType;


    public MysqlTriggerCondition(String schemaName, String tableName, MysqlEventType eventType) {
        super(AbstractEvent.EventType.MYSQL);
        this.schemaName = schemaName.trim();
        this.tableName = tableName.trim();
        this.eventType = eventType;
    }

    @Override
    public boolean match(Event<CanalEventInfo> event) {
        CanalEventInfo eventInfo = event.getData();
        if (!this.schemaName.equals(eventInfo.getSchemaName())) {
            return false;
        }

        if (!this.tableName.equals(eventInfo.getTableName())) {
            return false;
        }

        if (!this.eventType.equals(eventInfo.getEventType())) {
            return false;
        }

        return true;
    }
}
