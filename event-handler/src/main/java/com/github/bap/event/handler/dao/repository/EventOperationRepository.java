package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.DbEventOperationPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 周广
 **/
@Repository
public interface EventOperationRepository extends JpaRepository<DbEventOperationPO, Integer> {


    /**
     * 根据ruleId查找所有的操作监听
     *
     * @param ruleId 规则id
     * @return 操作集
     */
    List<DbEventOperationPO> findAllByRuleId(int ruleId);

}
