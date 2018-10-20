package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.DbEventFuncUseLogPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 周广
 **/
@Repository
public interface EventFuncUseLogRepository extends JpaRepository<DbEventFuncUseLogPO, Integer> {


}

