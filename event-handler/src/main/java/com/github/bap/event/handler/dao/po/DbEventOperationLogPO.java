package com.github.bap.event.handler.dao.po;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author 周广
 **/
@Entity
@Table(name = "db_event_operation_log", catalog = "")
public class DbEventOperationLogPO {
    private Integer id;
    private Integer operationId;
    private String eventData;
    private String runResult;
    private String repairResult;
    private String exceptionInfo;
    private Timestamp createTime;

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
    @Column(name = "operation_id", nullable = false)
    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    @Basic
    @Column(name = "event_data", nullable = false, length = -1)
    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    @Basic
    @Column(name = "run_result", nullable = false, length = 512)
    public String getRunResult() {
        return runResult;
    }

    public void setRunResult(String runResult) {
        this.runResult = runResult;
    }

    @Basic
    @Column(name = "repair_result", nullable = true, length = 512)
    public String getRepairResult() {
        return repairResult;
    }

    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }

    @Basic
    @Column(name = "exception_info", nullable = true)
    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbEventOperationLogPO that = (DbEventOperationLogPO) o;
        return id == that.id &&
                operationId == that.operationId &&
                Objects.equals(eventData, that.eventData) &&
                Objects.equals(runResult, that.runResult) &&
                Objects.equals(repairResult, that.repairResult) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operationId, eventData, runResult, repairResult, createTime);
    }
}
