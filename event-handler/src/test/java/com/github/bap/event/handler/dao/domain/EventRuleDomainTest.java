package com.github.bap.event.handler.dao.domain;

import com.github.bap.event.handler.dao.po.DbEventOperationPO;
import com.github.bap.event.handler.dao.po.DbEventRulePO;
import com.github.bap.event.handler.dao.repository.EventOperationLogRepository;
import com.github.bap.event.handler.dao.repository.EventOperationRepository;
import com.google.common.collect.Lists;
import com.github.bap.event.handler.controller.dto.response.EventOperationResDTO;
import com.github.bap.event.handler.controller.dto.response.EventRuleResDTO;
import com.github.bap.event.handler.dao.repository.EventRuleRepository;
import com.github.bap.event.handler.trigger.Operation;
import com.github.bap.event.handler.trigger.Trigger;
import com.github.bap.mysql.event.source.CanalEventInfo;
import com.github.bap.mysql.event.source.MysqlEvent;
import com.github.bap.mysql.event.source.MysqlEventType;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author 周广
 **/
public class EventRuleDomainTest {

    private EventRuleDomain eventRuleDomain;
    private EventOperationRepository eventOperationRepository;
    private EventRuleRepository eventRuleRepository;
    private EventOperationLogRepository eventOperationLogRepository;

    @Before
    public void setUp() throws Exception {
        eventOperationRepository = mock(EventOperationRepository.class);
        eventRuleRepository = mock(EventRuleRepository.class);
        eventOperationLogRepository = mock(EventOperationLogRepository.class);

    }

    @Test
    public void disableEventRule() {
        DbEventRulePO eventRule = new DbEventRulePO();
        Integer ruleId = 10;
        eventRule.setId(ruleId);
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, eventRule);
        when(eventRuleRepository.updateRuleStatus(ruleId, EventRuleDomain.RULE_OFF_STATUS)).thenReturn(1);
        Assert.assertTrue(eventRuleDomain.disableEventRule());
    }

    @Test
    public void removeOperation() {
        DbEventRulePO eventRule = new DbEventRulePO();
        int ruleId = 10;
        eventRule.setId(ruleId);
        DbEventOperationPO operationPO = new DbEventOperationPO();
        Integer operationId = 20;
        operationPO.setId(operationId);
        List<DbEventOperationPO> poList = Lists.newArrayList(operationPO);

        when(eventOperationRepository.findAllByRuleId(ruleId)).thenReturn(poList);
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, eventRule);
        Assert.assertTrue(eventRuleDomain.removeOperation(operationId));
    }

    @Test
    public void getRuleId() {
        DbEventRulePO eventRule = new DbEventRulePO();
        Integer ruleId = 10;
        eventRule.setId(ruleId);
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, eventRule);
        Assert.assertEquals(eventRuleDomain.getRuleId(), ruleId);
    }

    @Test
    public void saveEventRule() {
        Integer ruleId = 10;
        EventRuleResDTO ruleResDTO = new EventRuleResDTO();
        ruleResDTO.setEventType(MysqlEventType.DELETE.getType());
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, ruleResDTO);

        when(eventRuleRepository.findBySchemasNameAndTNameAndEventType(any(), any(), any())).thenReturn(null);

        DbEventRulePO eventRule = new DbEventRulePO();
        eventRule.setId(ruleId);
        when(eventRuleRepository.save(any())).thenReturn(eventRule);
        Assert.assertEquals(eventRuleDomain.saveEventRule(), ruleId);
    }

    @Test
    public void addOperation() {
        EventRuleResDTO ruleResDTO = new EventRuleResDTO();
        ruleResDTO.setEventType(MysqlEventType.DELETE.getType());
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, ruleResDTO);
        Integer operationId = 20;
        EventOperationResDTO resDTO = new EventOperationResDTO();
        resDTO.setId(operationId);
        Assert.assertTrue(eventRuleDomain.getOperationList().isEmpty());
        eventRuleDomain.addOperation(Lists.newArrayList(resDTO));
        Optional optional = eventRuleDomain.getOperationList()
                .stream().filter(s -> s.getId().equals(operationId)).findAny();
        Assert.assertTrue(optional.isPresent());

    }

    @Test
    public void updateOperation() {
        int ruleId = 10;
        Integer operationId = 20;

        DbEventRulePO eventRule = new DbEventRulePO();
        eventRule.setId(ruleId);
        DbEventOperationPO operationPO = new DbEventOperationPO();
        operationPO.setId(operationId);

        when(eventOperationRepository.findAllByRuleId(ruleId)).thenReturn(Lists.newArrayList(operationPO));
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, eventRule);

        EventOperationResDTO changeOperation = new EventOperationResDTO();
        changeOperation.setId(operationId);
        String script = "aaa";
        String remarks = "remark";
        String changeUser = "test";
        changeOperation.setOperationScript(script);
        changeOperation.setRemarks(remarks);
        changeOperation.setLastChangeUser(changeUser);
        eventRuleDomain.updateOperation(Lists.newArrayList(changeOperation));
        Optional<DbEventOperationPO> optional = eventRuleDomain.getOperationList()
                .stream().filter(s -> s.getId().equals(operationId)).findAny();
        Assert.assertTrue(optional.isPresent());
        DbEventOperationPO po = optional.get();
        Assert.assertEquals(po.getOperationScript(), script);
        Assert.assertEquals(po.getRemarks(), remarks);
        Assert.assertEquals(po.getLastChangeUser(), changeUser);
    }

    @Test
    public void createNewTrigger() {
        int ruleId = 10;
        Integer operationId = 20;
        String schemasName = "test_db";
        String tableName = "table_name";
        MysqlEventType mysqlType = MysqlEventType.QUERY;
        DbEventRulePO eventRule = new DbEventRulePO();
        eventRule.setId(ruleId);
        eventRule.setEventType(mysqlType.getType());
        eventRule.setSchemasName(schemasName);
        eventRule.settName(tableName);
        DbEventOperationPO operationPO = new DbEventOperationPO();
        operationPO.setId(operationId);
        operationPO.setDelayTime(10);
        operationPO.setOperationScript("function check_event(event) {\n" +
                "    //在这里输入js代码\n" +
                "\n" +
                "    return false;\n" +
                "}");
        List<DbEventOperationPO> poList = Lists.newArrayList(operationPO);

        when(eventOperationRepository.findAllByRuleId(ruleId)).thenReturn(poList);
        eventRuleDomain = new EventRuleDomain(eventOperationRepository,
                eventRuleRepository, eventOperationLogRepository, eventRule);

        Trigger<CanalEventInfo> trigger = eventRuleDomain.createNewTrigger(new ScheduledThreadPoolExecutor(
                1, new DefaultThreadFactory("Script-Exec-ThreadPool")));

        CanalEventInfo data = new CanalEventInfo();
        data.setEventType(mysqlType);
        data.setSchemaName(schemasName);
        data.setTableName(tableName);
        MysqlEvent event = new MysqlEvent(data);
        Assert.assertTrue(trigger.getTriggerCondition().match(event));

        Optional<Operation<CanalEventInfo>> operation = trigger.getOperation()
                .stream().filter(s -> s.getOperationId().equals(String.valueOf(operationId))).findFirst();
        Assert.assertTrue(operation.isPresent());
    }
}