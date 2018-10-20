package com.github.bap.event.handler.controller;

import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 周广
 **/
public abstract class AbstractController {
    @Autowired
    private UserService userService;

    protected String getLoginName(HttpServletRequest request) {
        String sid = request.getHeader("accessToken");
        if (StringUtils.isBlank(sid) || !userService.verifySessionId(sid)) {
            throw new ServerException("登录超时");
        }
        return userService.getLoginName(sid);
    }

}
