package com.github.bap.event.handler.service;

import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.domain.UserDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 周广
 **/
@Service
public class UserService {
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private SessionService sessionService;

    /**
     * 尝试登录，登录成功缓存sid和用户信息
     *
     * @param loginName     登录名称
     * @param loginPassword 登录密码
     * @return session id
     */
    public String login(String loginName, String loginPassword) {
        UserDomain userDomain = domainFactory.createUserDomain(loginName);
        String sid = userDomain.login(loginPassword);
        sessionService.saveSessionId(sid, loginName);
        return sid;
    }

    /**
     * 注销登录
     *
     * @param sid session id
     */
    public void logout(String sid) {
        sessionService.removeSid(sid);
    }


    /**
     * 校验session id
     *
     * @param sid session id
     * @return 是否登录
     */
    public boolean verifySessionId(String sid) {
        return sessionService.verifySessionId(sid);
    }

    public String getLoginName(String sid) {
        return sessionService.getLoginName(sid);
    }

}
