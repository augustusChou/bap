package com.github.bap.event.handler.dao.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author 周广
 **/
@Entity
@Table(name = "db_event_rule", catalog = "")
public class DbEventRulePO {
    private Integer id;
    private String schemasName;
    private String tName;
    private Integer eventType;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String ruleStatus;
    private String creator;
    private String lastChangeUser;


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
    @Column(name = "schemas_name", nullable = false, length = 32)
    public String getSchemasName() {
        return schemasName;
    }

    public void setSchemasName(String schemasName) {
        this.schemasName = schemasName;
    }

    @Basic
    @Column(name = "t_name", nullable = true, length = 64)
    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    @Basic
    @Column(name = "event_type", nullable = true)
    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
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
    @Column(name = "rule_status", nullable = false, length = 1)
    public String getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(String ruleStatus) {
        this.ruleStatus = ruleStatus;
    }

    @Basic
    @Column(name = "creator", nullable = true, length = 64)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbEventRulePO that = (DbEventRulePO) o;
        return id == that.id &&
                Objects.equals(schemasName, that.schemasName) &&
                Objects.equals(tName, that.tName) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, schemasName, tName, eventType, createTime, updateTime);
    }
}
