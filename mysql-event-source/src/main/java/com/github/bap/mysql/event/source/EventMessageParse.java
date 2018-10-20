package com.github.bap.mysql.event.source;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.collect.Lists;
import com.github.bap.event.source.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.sql.JDBCType;
import java.util.*;

/**
 * @author 周广
 **/
@Slf4j
public class EventMessageParse implements CanalMessageParse<CanalEventInfo> {

    @Override
    public List<Event<CanalEventInfo>> convertEvent(Message message) {
        List<CanalEntry.Entry> entryList = message.getEntries();
        if (entryList == null || entryList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Event<CanalEventInfo>> list = Lists.newArrayListWithCapacity(entryList.size());
        for (CanalEntry.Entry entry : entryList) {
            try {
                CanalEventInfo info = parseRowData(entry);
                if (info != null) {
                    info.setExecuteTime(entry.getHeader().getExecuteTime());
                    info.setSchemaName(entry.getHeader().getSchemaName().toLowerCase().trim());
                    info.setTableName(entry.getHeader().getTableName().toLowerCase().trim());
                    list.add(new MysqlEvent(info));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 解析RowData 返回事件解析对象 如果返回null代表解析失败、或者EntryType不是ROWDATA
     *
     * @param entry canal事件实体
     * @return 事件解析对象或者null
     */
    @Nullable
    private CanalEventInfo parseRowData(CanalEntry.Entry entry) {
        if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                log.warn(String.format("解析事件Row数据错误 , data:%s", entry.toString()), e);
                return null;
            }

            MysqlEventType eventType = MysqlEventType.convertEventType(rowChange.getEventType());
            if (eventType == null) {
                log.info("只支持MysqlEventType存在的事件类型，不支持:{}", rowChange.getEventType());
                return null;
            }

            if (eventType == MysqlEventType.QUERY || rowChange.getIsDdl()) {
                log.info("查询或者ddl操作 sql ----> {}", rowChange.getSql());
                return null;
            }


            Map<String, Integer> sqlType = new LinkedHashMap<>();
            Map<String, String> mysqlType = new LinkedHashMap<>();
            List<Map<String, String>> data = new ArrayList<>();
            List<Map<String, String>> old = new ArrayList<>();
            Set<String> updateSet = new HashSet<>();
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                Map<String, String> row = new LinkedHashMap<>();
                List<CanalEntry.Column> columns;

                if (eventType == MysqlEventType.DELETE) {
                    columns = rowData.getBeforeColumnsList();
                } else {
                    columns = rowData.getAfterColumnsList();
                }

                for (CanalEntry.Column column : columns) {
                    sqlType.put(column.getName(), column.getSqlType());
                    mysqlType.put(column.getName(), column.getMysqlType());
                    row.put(column.getName(), column.getValue());
                    // 获取update为true的字段
                    if (column.getUpdated()) {
                        updateSet.add(column.getName());
                    }
                }
                if (!row.isEmpty()) {
                    data.add(row);
                }

                if (eventType == MysqlEventType.UPDATE) {
                    Map<String, String> rowOld = new LinkedHashMap<>();
                    for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                        if (updateSet.contains(column.getName())) {
                            rowOld.put(column.getName(), column.getValue());
                        }
                    }
                    // update操作将记录修改前的值
                    if (!rowOld.isEmpty()) {
                        old.add(rowOld);
                    }
                }
            }

            CanalEventInfo info = new CanalEventInfo();
            info.setEventType(eventType);
            info.setSql(rowChange.getSql());
            if (!sqlType.isEmpty()) {
                info.setSqlType(sqlType);
            }
            if (!mysqlType.isEmpty()) {
                info.setMysqlType(mysqlType);
            }
            if (!data.isEmpty()) {
                info.setData(data);
            }
            if (!old.isEmpty()) {
                info.setOld(old);
            }
            return info;
        }
        return null;
    }


    private List<Map<String, Object>> convertToJavaTypeValue(List<Map<String, String>> data, Map<String, Integer> sqlType) {
        List<Map<String, Object>> newData = Lists.newArrayListWithCapacity(data.size());

        for (Map<String, String> column : data) {
            Map<String, Object> newMap = new HashMap<>(column.size());
            newData.add(newMap);

            column.forEach((key, value) -> {
                if (!sqlType.containsKey(key)) {
                    return;
                }
                JDBCType jdbcType = JDBCType.valueOf(sqlType.get(key));
                newMap.put(key, convertToJavaTypeValue(value, jdbcType));
            });
        }

        return newData;
    }

    /**
     * 将字符串类型值转化为jdbcType对应的java类型
     *
     * @param originalValue 原始数据
     * @param jdbcType      原始数据对应的jdbc类型
     * @return jdbc类型对应的java类型值 注意，只对基础类型进行了转化，日期和二进制类型还是字符串
     */
    private Object convertToJavaTypeValue(String originalValue, JDBCType jdbcType) {
        if (StringUtils.isBlank(originalValue)) {
            return null;
        }
        switch (jdbcType) {
            case CHAR:
            case VARCHAR:
            case LONGVARCHAR:
                return originalValue;
            case NUMERIC:
            case DECIMAL:
                return new java.math.BigDecimal(originalValue);
            case BIT:
            case BOOLEAN:
                return Boolean.valueOf(originalValue);
            case TINYINT:
                return Byte.valueOf(originalValue);
            case SMALLINT:
                return Short.valueOf(originalValue);
            case INTEGER:
                return Integer.valueOf(originalValue);
            case BIGINT:
                return Long.valueOf(originalValue);
            case REAL:
                return Float.valueOf(originalValue);
            case FLOAT:
            case DOUBLE:
                return Double.valueOf(originalValue);
            default:
                return originalValue;
        }
    }

}
