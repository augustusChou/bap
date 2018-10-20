package com.github.bap.event.handler.controller.dto.response;

import com.github.bap.common.LocalDateTimeUtil;
import com.github.bap.event.handler.dao.po.DbEventOperationLogPO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author 周广
 **/
@Data
public class EventOperationLogResDTO implements Serializable {


    private Integer id;
    private Integer operationId;
    private String eventData;
    private String runResult;
    private String repairResult;
    private String createTime;

    public static EventOperationLogResDTO convert(DbEventOperationLogPO po) {
        EventOperationLogResDTO dto = new EventOperationLogResDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setCreateTime(LocalDateTimeUtil.convertString(po.getCreateTime()));
        return dto;
    }


}
