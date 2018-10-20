package com.github.bap.event.handler.controller.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class LoginReqDTO implements Serializable {

    @NotNull
    private String loginName;
    @NotNull
    private String loginPassword;

}
