package com.github.bap.event.handler.controller;

import com.github.bap.event.handler.controller.dto.request.LoginReqDTO;
import com.github.bap.event.handler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 周广
 **/
@RestController
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping(value = "login")
    public ResultData<String> login(@RequestBody @Valid LoginReqDTO dto) {
        return ResultData.success(userService.login(dto.getLoginName(), dto.getLoginPassword()));
    }

    @PostMapping(value = "logout")
    public ResultData logout(@RequestParam(value = "sid") String sid) {
        userService.logout(sid);
        return ResultData.success();
    }
}
