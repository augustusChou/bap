package com.github.bap.event.handler.dao.domain;

import com.alibaba.fastjson.JSON;
import com.github.bap.event.handler.dao.po.DbEventOperationLogPO;
import com.github.bap.event.handler.dao.po.DbEventOperationPO;
import com.github.bap.event.handler.dao.po.DbEventRulePO;
import com.github.bap.event.handler.dao.repository.EventOperationLogRepository;
import com.github.bap.event.handler.dao.repository.EventOperationRepository;
import com.google.common.collect.Lists;
import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.controller.dto.response.EventOperationResDTO;
import com.github.bap.event.handler.controller.dto.response.EventRuleResDTO;
import com.github.bap.event.handler.dao.repository.EventRuleRepository;
import com.github.bap.event.handler.trigger.Condition;
import com.github.bap.event.handler.trigger.Trigger;
import com.github.bap.event.handler.trigger.mysql.MysqlOperation;
import com.github.bap.event.handler.trigger.mysql.MysqlTrigger;
import com.github.bap.event.handler.trigger.mysql.MysqlTriggerCondition;
import com.github.bap.mysql.event.source.CanalEventInfo;
import com.github.bap.mysql.event.source.MysqlEventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 周广
 **/
@Slf4j
public class EventRuleDomain {

    public static final String RULE_ON_STATUS = "1";
    public static final String RULE_OFF_STATUS = "0";
    private EventOperationRepository eventOperationRepository;
    private EventRuleRepository eventRuleRepository;
    private EventOperationLogRepository eventOperationLogRepository;
    private DbEventRulePO eventRule;
    private List<DbEventOperationPO> operationList;
    private MysqlEventType eventType;


    public EventRuleDomain(EventOperationRepository eventOperationRepository,
                           EventRuleRepository eventRuleRepository,
                           EventOperationLogRepository eventOperationLogRepository,
                           DbEventRulePO eventRule) {
        this.eventOperationRepository = eventOperationRepository;
        this.eventRuleRepository = eventRuleRepository;
        this.eventOperationLogRepository = eventOperationLogRepository;
        this.eventRule = eventRule;
        this.operationList = eventOperationRepository.findAllByRuleId(this.eventRule.getId());
        this.eventType = MysqlEventType.convert(this.eventRule.getEventType());
    }

    public EventRuleDomain(EventOperationRepository eventOperationRepository,
                           EventRuleRepository eventRuleRepository,
                           EventOperationLogRepository eventOperationLogRepository,
                           EventRuleResDTO ruleResDTO) {
        this.eventOperationRepository = eventOperationRepository;
        this.eventRuleRepository = eventRuleRepository;
        this.eventOperationLogRepository = eventOperationLogRepository;

        if (ruleResDTO.getId() == null) {
            this.eventRule = new DbEventRulePO();
            this.eventRule.setCreator(ruleResDTO.getCreator());
            this.eventRule.setLastChangeUser(ruleResDTO.getLastChangeUser());
            this.eventRule.setCreateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            this.eventRule = eventRuleRepository.findById(ruleResDTO.getId()).orElse(null);
            if (this.eventRule == null) {
                throw new ServerException("事件规则id错误");
            }
            this.eventRule.setLastChangeUser(ruleResDTO.getLastChangeUser());
            this.eventRule.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }
        this.eventRule.setEventType(ruleResDTO.getEventType());
        this.eventRule.setSchemasName(ruleResDTO.getSchemasName());
        this.eventRule.settName(ruleResDTO.getTName());
        this.eventRule.setRuleStatus(ruleResDTO.getRuleStatus());

        if (this.eventRule.getId() != null) {
            this.operationList = eventOperationRepository.findAllByRuleId(this.eventRule.getId());
        } else {
            this.operationList = Lists.newArrayList();
        }
        this.eventType = MysqlEventType.convert(this.eventRule.getEventType());
    }


    /**
     * 禁用事件规则
     */
    public boolean disableEventRule() {
        return this.eventRuleRepository.updateRuleStatus(this.eventRule.getId(), RULE_OFF_STATUS) > 0;
    }

    /**
     * 删除操作
     *
     * @param operationId 操作id
     */
    public boolean removeOperation(Integer operationId) {
        this.eventOperationRepository.deleteById(operationId);
        return this.operationList.removeIf(o -> o.getId().equals(operationId));
    }


    public Integer getRuleId() {
        return this.eventRule.getId();
    }

    /**
     * 保存事件规则
     */
    public Integer saveEventRule() {
        try {
            this.eventRule = this.eventRuleRepository.save(this.eventRule);
        } catch (Exception e) {
            throw new ServerException("数据库名、表名、查询类型对应的事件规则已经存在");
        }
        return this.eventRule.getId();
    }

    /**
     * 添加操作list
     *
     * @param operationResDTOList 操作list
     */
    public void addOperation(List<EventOperationResDTO> operationResDTOList) {
        if (operationResDTOList != null) {
            for (EventOperationResDTO operation : operationResDTOList) {
                addOperation(operation.convert());
            }
        }
    }

    /**
     * 添加操作
     *
     * @param eventOperation 事件操作
     */
    private void addOperation(DbEventOperationPO eventOperation) {
        eventOperation.setRuleId(this.eventRule.getId());
        this.eventOperationRepository.save(eventOperation);
        this.operationList.add(eventOperation);
    }


    public void updateOperation(List<EventOperationResDTO> operationResDTOList) {
        if (operationResDTOList != null) {
            for (EventOperationResDTO operation : operationResDTOList) {
                updateOperation(operation.convert());
            }
        }
    }

    private void updateOperation(DbEventOperationPO newPO) {
        for (DbEventOperationPO po : this.operationList) {
            if (po.getId().equals(newPO.getId())) {
                po.setLastChangeUser(newPO.getLastChangeUser());
                if (StringUtils.isNotBlank(newPO.getRemarks())) {
                    po.setRemarks(newPO.getRemarks());
                }
                if (StringUtils.isNotBlank(newPO.getOperationScript())) {
                    po.setOperationScript(newPO.getOperationScript());
                }
                if (newPO.getDelayTime() != null) {
                    po.setDelayTime(newPO.getDelayTime());
                }
                this.eventOperationRepository.save(po);
            }
        }
    }

    List<DbEventOperationPO> getOperationList() {
        return operationList;
    }

    /**
     * 将规则转化为触发器，一条规则记录对应一个触发器
     *
     * @return 返回规则转化后的触发器
     */
    public Trigger<CanalEventInfo> createNewTrigger(ScheduledExecutorService operationExecPool) {
        Condition<CanalEventInfo> condition = new MysqlTriggerCondition(
                this.eventRule.getSchemasName(),
                this.eventRule.gettName(),
                this.eventType);

        Trigger<CanalEventInfo> trigger = new MysqlTrigger(condition);

        for (DbEventOperationPO operationPO : this.operationList) {
            MysqlOperation mysqlOperation;
            try {
                mysqlOperation = new MysqlOperation(String.valueOf(operationPO.getId()),
                        operationPO.getOperationScript(), operationExecPool, operationPO.getDelayTime());
                //设置日志处理器函数
                mysqlOperation.setLogHandler((event, logData) -> {
                    DbEventOperationLogPO log = new DbEventOperationLogPO();
                    log.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    log.setEventData(JSON.toJSONString(event));
                    log.setOperationId(operationPO.getId());
                    MysqlOperation.LogInfo logInfo = (MysqlOperation.LogInfo) logData;
                    log.setRunResult(logInfo.getRunResult());
                    log.setRepairResult(logInfo.getRepairResult());
                    if (logInfo.getScriptExecException() != null) {
                        log.setExceptionInfo(convertException(logInfo.getScriptExecException()));
                    }
                    eventOperationLogRepository.save(log);
                });
            } catch (Exception e) {
                log.error("脚本校验失败", e);
                continue;
            }

            trigger.addTriggerOperation(mysqlOperation);
        }

        return trigger;
    }

    private String convertException(Throwable e) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);) {
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (IOException e1) {
            e1.printStackTrace();
            return e.getMessage();
        }
    }


}
