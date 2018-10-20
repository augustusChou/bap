package com.github.bap.event.handler.controller.dto.response;

import com.github.bap.common.LocalDateTimeUtil;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import com.github.bap.event.handler.dao.po.DbEventFuncConfigPO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class FuncConfigInfoDTO implements Serializable {


    private Integer id;
    private String configType;
    private String configTypeDesc;
    private String configName;
    private String configInfo;
    private String createTime;
    private String updateTime;


    public static FuncConfigInfoDTO convert(DbEventFuncConfigPO po) {
        FuncConfigInfoDTO dto = new FuncConfigInfoDTO();
        BeanUtils.copyProperties(po, dto);
        EventFunctionConfigDomain.FunctionEnum functionEnum = EventFunctionConfigDomain.FunctionEnum.convent(po.getConfigType());
        dto.setConfigType(functionEnum.getType());
        dto.setConfigTypeDesc(functionEnum.getDesc());
        dto.setCreateTime(LocalDateTimeUtil.convertString(po.getCreateTime()));
        dto.setUpdateTime(LocalDateTimeUtil.convertString(po.getUpdateTime()));
        return dto;
    }
}
