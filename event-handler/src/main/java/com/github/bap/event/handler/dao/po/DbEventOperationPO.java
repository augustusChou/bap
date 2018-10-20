package com.github.bap.event.handler.dao.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author 周广
 **/
@Entity
@Table(name = "db_event_operation",  catalog = "")
public class DbEventOperationPO {
    private Integer id;
    private Integer ruleId;
    private String operationScript;
    private String remarks;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String creator;
    private String lastChangeUser;
    private Integer delayTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "rule_id", nullable = false)
    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    @Basic
    @Column(name = "operation_script", nullable = false, length = -1)
    public String getOperationScript() {
        return operationScript;
    }

    public void setOperationScript(String operationScript) {
        this.operationScript = operationScript;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = false)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "remarks", nullable = false)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "creator", nullable = false)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "last_change_user", nullable = false)
    public String getLastChangeUser() {
        return lastChangeUser;
    }

    public void setLastChangeUser(String lastChangeUser) {
        this.lastChangeUser = lastChangeUser;
    }

    @Basic
    @Column(name = "delay_time")
    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbEventOperationPO that = (DbEventOperationPO) o;
        return id == that.id &&
                ruleId == that.ruleId &&
                Objects.equals(operationScript, that.operationScript) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, ruleId, operationScript, createTime, updateTime);
    }
}
