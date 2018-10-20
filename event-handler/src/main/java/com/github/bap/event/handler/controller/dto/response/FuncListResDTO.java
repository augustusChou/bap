package com.github.bap.event.handler.controller.dto.response;

import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class FuncListResDTO implements Serializable {

    private String type;
    private String desc;


    public FuncListResDTO(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FuncListResDTO convert(EventFunctionConfigDomain.FunctionEnum functionEnum) {
        return new FuncListResDTO(functionEnum.getType(), functionEnum.getDesc());
    }

}
