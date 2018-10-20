package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.DbEventRulePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 周广
 **/
@Repository
public interface EventRuleRepository extends JpaRepository<DbEventRulePO, Integer> {


    /**
     * 根据id更新事件规则的状态
     *
     * @param id         事件id
     * @param ruleStatus 事件规则状态
     * @return 影响结果数
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value = "update db_event_rule set rule_status=?2 where id=?1", nativeQuery = true)
    int updateRuleStatus(Integer id, String ruleStatus);


    /**
     * 根据状态查询事件规则
     *
     * @param ruleStatus 事件规则状态  1是启用 0是禁用
     * @return 结果list
     */
    List<DbEventRulePO> findAllByRuleStatus(String ruleStatus);


    /**
     * 根据3个字段查询记录（走索引）
     *
     * @param schemasName 数据库名称
     * @param tName       表名
     * @param eventType   查询类型
     * @return 返回是否有记录
     */
    DbEventRulePO findBySchemasNameAndTNameAndEventType(String schemasName, String tName, Integer eventType);
}

