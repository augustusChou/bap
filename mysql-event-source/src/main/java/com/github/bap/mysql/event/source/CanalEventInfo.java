package com.github.bap.mysql.event.source;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * @author 周广
 **/
@Data
@Slf4j
public class CanalEventInfo implements Serializable {


    /**
     * binlog里记录变更发生的时间戳,精确到秒
     */
    private long executeTime;

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
    @Nullable
    private MysqlEventType eventType;


    /**
     * 具体的sql
     */
    private String sql;


    /**
     * key是列名 类似："banner_name" -> "12"
     * value是 java.sql.Types的值
     */
    private Map<String, Integer> sqlType;

    /**
     * key是列名 类似："banner_name" -> "varchar(50)"
     */
    private Map<String, String> mysqlType;

    /**
     * data含所有列
     * 变更后的数据字段 insert只有after columns 而update则会有before / after columns数据.
     * key是列名
     */
    private List<Map<String, String>> data;
    /**
     * 注意，old只含有变动的列
     * 变更前的数据字段 delete只有before columns 而update则会有before / after columns数据.
     * key是列名
     */
    private List<Map<String, String>> old;

}
