package com.github.bap.event.handler.dao.domain;

import com.github.bap.event.handler.dao.po.DbEventRulePO;
import com.github.bap.event.handler.dao.repository.*;
import com.github.bap.event.handler.controller.dto.response.EventRuleResDTO;
import com.github.bap.event.handler.controller.dto.response.FuncConfigInfoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 周广
 **/
@Component
public class DomainFactory {

    @Autowired
    private EventOperationRepository eventOperationRepository;
    @Autowired
    private EventRuleRepository eventRuleRepository;
    @Autowired
    private EventOperationLogRepository eventOperationLogRepository;
    @Autowired
    private EventFuncConfigRepository eventFuncConfigRepository;
    @Autowired
    private OperationUserRepository operationUserRepository;

    public EventRuleDomain createEventRuleDomain(DbEventRulePO eventRule) {
        return new EventRuleDomain(
                eventOperationRepository,
                eventRuleRepository,
                eventOperationLogRepository,
                eventRule);
    }

    public EventRuleDomain createEventRuleDomain(EventRuleResDTO ruleResDTO) {
        return new EventRuleDomain(
                eventOperationRepository,
                eventRuleRepository,
                eventOperationLogRepository,
                ruleResDTO);
    }

    public EventFunctionConfigDomain createEventFunctionConfigDomain(EventFunctionConfigDomain.FunctionEnum functionEnum, String configName) {
        return new EventFunctionConfigDomain(functionEnum, configName, eventFuncConfigRepository);
    }


    public EventFunctionConfigDomain createEventFunctionConfigDomain(FuncConfigInfoDTO configInfoDTO) {
        return new EventFunctionConfigDomain(configInfoDTO, eventFuncConfigRepository);
    }


    public UserDomain createUserDomain(String loginName) {
        return new UserDomain(loginName, operationUserRepository);
    }


}
