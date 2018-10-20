package com.github.bap.event.handler.controller.dto.response;

import com.github.bap.common.LocalDateTimeUtil;
import com.github.bap.event.handler.dao.po.DbEventRulePO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author 周广
 **/
@Data
public class EventRuleResDTO implements Serializable {

    private Integer id;
    @NotNull
    private String schemasName;
    @NotNull
    private String tName;
    @NotNull
    private Integer eventType;
    private String createTime;
    private String updateTime;
    @NotNull
    private String ruleStatus;
    private List<EventOperationResDTO> operationList;
    private String creator;
    private String lastChangeUser;

    public static EventRuleResDTO convert(DbEventRulePO po) {
        EventRuleResDTO dto = new EventRuleResDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setCreateTime(LocalDateTimeUtil.convertString(po.getCreateTime()));
        dto.setUpdateTime(LocalDateTimeUtil.convertString(po.getUpdateTime()));
        return dto;
    }

}
