package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.DbEventFuncConfigPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 周广
 **/
@Repository
public interface EventFuncConfigRepository extends JpaRepository<DbEventFuncConfigPO, Integer> {


    /**
     * 查询java提供给js的功能的配置信息
     *
     * @param configType 0:数据库 1:redis 2:钉钉报警
     * @param configName 配置名称，比如数据库就是数据库名称。redis就是0-15这种数据库名称
     * @return 功能配置信息
     */
    DbEventFuncConfigPO findByConfigTypeAndConfigName(String configType, String configName);

}
