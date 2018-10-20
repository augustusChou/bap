package com.github.bap.event.handler.controller.dto.response;

import com.github.bap.common.LocalDateTimeUtil;
import com.github.bap.event.handler.dao.po.DbEventOperationPO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 周广
 **/
@Data
public class EventOperationResDTO implements Serializable {

    private Integer id;
    private Integer ruleId;
    private String operationScript;
    private String remarks;
    private String createTime;
    private String updateTime;
    private String creator;
    private String lastChangeUser;
    private Integer delayTime;

    public static EventOperationResDTO convert(DbEventOperationPO po) {
        EventOperationResDTO dto = new EventOperationResDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setCreateTime(LocalDateTimeUtil.convertString(po.getCreateTime()));
        dto.setUpdateTime(LocalDateTimeUtil.convertString(po.getUpdateTime()));
        return dto;
    }


    public DbEventOperationPO convert() {
        DbEventOperationPO po = new DbEventOperationPO();
        BeanUtils.copyProperties(this, po);
        if (this.createTime != null) {
            po.setCreateTime(LocalDateTimeUtil.parseToTimestamp(this.createTime));
        } else {
            po.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        po.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (delayTime == null) {
            po.setDelayTime(0);
        }
        return po;
    }
}
