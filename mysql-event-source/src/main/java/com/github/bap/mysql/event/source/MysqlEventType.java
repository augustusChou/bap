package com.github.bap.mysql.event.source;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.jetbrains.annotations.Nullable;

/**
 * @author 周广
 **/
public enum MysqlEventType {

    /**
     * 查询
     */
    QUERY(0),
    /**
     * 新增
     */
    INSERT(1),
    /**
     * 修改
     */
    UPDATE(2),
    /**
     * 删除
     */
    DELETE(3);

    private Integer type;

    MysqlEventType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    /**
     * 将canal事件类型转为内部事件类型
     *
     * @param type canal事件类型
     * @return 内部事件类型 可能为null
     */
    @Nullable
    public static MysqlEventType convertEventType(CanalEntry.EventType type) {
        switch (type) {
            case QUERY:
                return QUERY;
            case INSERT:
                return INSERT;
            case UPDATE:
                return UPDATE;
            case DELETE:
                return DELETE;
            default:
        }
        return null;
    }

    /**
     * 将数据库
     * @param type
     * @return
     */
    @Nullable
    public static MysqlEventType convert(Integer type) {
        for (MysqlEventType eventType : values()) {
            if (eventType.type.equals(type)) {
                return eventType;
            }
        }
        return null;
    }

}
