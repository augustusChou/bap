package com.github.bap.event.handler.script.func;

import com.alibaba.fastjson.JSONObject;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * java提供给js调用的函数
 *
 * @author 周广
 **/
public class MysqlFunction {


    /**
     * key是数据库名称
     */
    private static Map<String, JdbcTemplate> TEMPLATE_MAP = new ConcurrentHashMap<>();
    private static Map<String, Integer> DATABASE_CONFIG_ID_MAP = new ConcurrentHashMap<>();
    private static ResultSetExtractor<Map<String, Object>> SELECT_EXTRACTOR = new SelectResultSetExtractor();

    /**
     * 获取数据库对应的操作模板
     *
     * @param databaseName 数据库名称
     * @return jdbc操作模板
     */
    private static JdbcTemplate getJdbcTemplate(String databaseName) {
        if (TEMPLATE_MAP.containsKey(databaseName)) {
            return TEMPLATE_MAP.get(databaseName);
        }

        synchronized (MysqlFunction.class) {
            if (TEMPLATE_MAP.containsKey(databaseName)) {
                return TEMPLATE_MAP.get(databaseName);
            }
            EventFunctionConfigDomain functionConfigDomain = FunctionBean
                    .getFunctionBean()
                    .getDomainFactory()
                    .createEventFunctionConfigDomain(EventFunctionConfigDomain.FunctionEnum.MYSQL_DATABASE, databaseName);
            DATABASE_CONFIG_ID_MAP.put(databaseName, functionConfigDomain.getConfigId());
            JdbcTemplate jdbcTemplate = functionConfigDomain.getJdbcTemplate();
            TEMPLATE_MAP.put(databaseName, jdbcTemplate);
            return jdbcTemplate;
        }
    }

    /**
     * 执行增删改sql
     *
     * @param databaseName 数据库名称（配置名称）
     * @param sql          sql
     * @return 更新结果
     */
    public static int update(String databaseName, String sql) {
        log(databaseName, sql, null);
        return getJdbcTemplate(databaseName).update(sql);
    }


    /**
     * 执行查询sql 返回一条记录
     *
     * @param databaseName 数据库名称（配置名称）
     * @param sql          sql
     * @return 单个记录对象或者null
     */
    public static Object selectOne(String databaseName, String sql) {
        log(databaseName, sql, null);
        return getJdbcTemplate(databaseName).query(sql, SELECT_EXTRACTOR);
    }

    /**
     * 执行查询sql 返回一条记录
     *
     * @param databaseName 数据库名称（配置名称）
     * @param sql          sql 参数使用 ? 在占位符
     *                     例如： select * from table where name=? age=?  那么name是args[0] age是args[1]
     * @param args         参数列表
     * @return 单个记录对象或者null
     */
    public static Object selectOne(String databaseName, String sql, Object[] args) {
        log(databaseName, sql, args);
        return getJdbcTemplate(databaseName).query(sql, args, SELECT_EXTRACTOR);
    }


    /**
     * 执行查询sql 返回list
     *
     * @param databaseName 数据库名称（配置名称）
     * @param sql          sql
     * @return 空list或者有数据的list
     */
    public static List<Map<String, Object>> selectAll(String databaseName, String sql) {
        log(databaseName, sql, null);
        return getJdbcTemplate(databaseName).queryForList(sql);
    }


    private static void log(String databaseName, String sql, Object[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("databaseName", databaseName);
        jsonObject.put("sql", sql);
        jsonObject.put("args", args);
        FunctionBean.getFunctionBean().logFunctionUse(getConfigId(databaseName), jsonObject.toJSONString());
    }

    private static Integer getConfigId(String databaseName) {
        if (!DATABASE_CONFIG_ID_MAP.containsKey(databaseName)) {
            getJdbcTemplate(databaseName);
        }
        return DATABASE_CONFIG_ID_MAP.get(databaseName);
    }

    static class SelectResultSetExtractor implements ResultSetExtractor<Map<String, Object>> {

        @Override
        public Map<String, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            ResultSetMetaData metaData = resultSet.getMetaData();
            Map<String, Object> columnMap = Maps.newHashMapWithExpectedSize(metaData.getColumnCount());
            while (resultSet.next()) {
                //列是从1开始
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    columnMap.put(columnName, resultSet.getObject(columnName));
                }
            }
            return columnMap;
        }
    }

}
