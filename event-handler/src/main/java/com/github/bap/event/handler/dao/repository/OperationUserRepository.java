package com.github.bap.event.handler.dao.repository;

import com.github.bap.event.handler.dao.po.OperationUserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 周广
 **/
@Repository
public interface OperationUserRepository extends JpaRepository<OperationUserPO, Integer> {

    /**
     * 根据登录名称查找用户
     *
     * @param loginName 登录名称
     * @return 用户的信息
     */
    OperationUserPO findByLoginName(String loginName);

}

