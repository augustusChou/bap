package com.github.bap.event.handler.dao.domain;

import com.github.bap.common.UUIDUtil;
import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.dao.po.OperationUserPO;
import com.github.bap.event.handler.dao.repository.OperationUserRepository;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author 周广
 **/
public class UserDomain implements Serializable {

    private OperationUserRepository operationUserRepository;
    private String loginName;
    private OperationUserPO operationUserPO;


    public UserDomain(String loginName, OperationUserRepository operationUserRepository) {
        this.operationUserRepository = operationUserRepository;
        this.loginName = loginName;
        this.operationUserPO = this.operationUserRepository.findByLoginName(this.loginName);
    }

    /**
     * 尝试登录
     *
     * @param loginPassword 登录密码
     * @return 登录成功返回sessionId
     */
    public String login(String loginPassword) {
        if (verifyLoginPassword(loginPassword)) {
            return UUIDUtil.getUUID();
        }
        throw new ServerException("登录失败");
    }


    /**
     * 校验登录密码
     *
     * @param loginPassword 登录密码
     * @return 登录密码是否正确
     */
    public boolean verifyLoginPassword(String loginPassword) {
        if (StringUtils.isBlank(loginPassword)) {
            return false;
        }
        return loginPassword.equals(this.operationUserPO.getLoginPassword());
    }

}
