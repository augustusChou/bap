package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.DbEventOperationLogPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 周广
 **/
@Repository
public interface EventOperationLogRepository extends JpaRepository<DbEventOperationLogPO, Integer> {


}
