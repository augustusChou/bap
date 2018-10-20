package com.github.bap.event.handler.controller.dto.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class EventRuleReqDTO implements Serializable {

    private Integer pageNo;
    private Integer pageSize;
    private String schemasName;
    private String tName;
    private Integer eventType;


    public Integer getPageNo() {
        return pageNo == null ? 1 : pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public String getSchemasName() {
        return StringUtils.isBlank(schemasName) ? null : schemasName;
    }

    public String gettName() {
        return StringUtils.isBlank(tName) ? null : tName;

    }
}
