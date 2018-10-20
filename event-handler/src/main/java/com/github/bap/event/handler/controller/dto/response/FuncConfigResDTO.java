package com.github.bap.event.handler.controller.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 周广
 **/
@Data
public class FuncConfigResDTO implements Serializable {

    private PageResponse<FuncConfigInfoDTO> pageResponse;
    private List<FuncListResDTO> funcList;

    public FuncConfigResDTO(PageResponse<FuncConfigInfoDTO> pageResponse, List<FuncListResDTO> funcList) {
        this.pageResponse = pageResponse;
        this.funcList = funcList;
    }
}
