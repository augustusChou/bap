package com.github.bap.event.handler.service;

import com.github.bap.event.handler.controller.dto.response.EventOperationResDTO;
import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.domain.EventRuleDomain;
import com.github.bap.event.handler.dao.repository.EventOperationLogRepository;
import com.github.bap.event.handler.dao.repository.EventOperationRepository;
import com.google.common.collect.Lists;
import com.github.bap.event.handler.core.MysqlEventHandlerInitialize;
import com.github.bap.event.handler.dao.repository.EventRuleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 周广
 **/
@Service
public class EventOperationService {
    @Autowired
    private EventRuleRepository eventRuleRepository;
    @Autowired
    private EventOperationLogRepository eventOperationLogRepository;
    @Autowired
    private DomainFactory domainFactory;
    @Autowired
    private EventOperationRepository eventOperationRepository;
    @Autowired
    private MysqlEventHandlerInitialize mysqlEventHandlerInitialize;


    public void addEventOperation(EventOperationResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(
                eventRuleRepository.findById(dto.getRuleId()).orElse(null));
        //将操作存储到数据库
        ruleDomain.addOperation(Lists.newArrayList(dto));
        //使用新配置生成新的触发器替换旧的触发器
        mysqlEventHandlerInitialize.registerOrReplaceConsumer(ruleDomain);
    }

    public void updateEventOperation(EventOperationResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(
                eventRuleRepository.findById(dto.getRuleId()).orElse(null));
        //将操作list存储到数据库
        ruleDomain.updateOperation(Lists.newArrayList(dto));
        if (StringUtils.isNotBlank(dto.getOperationScript())) {
            //使用新配置生成新的触发器替换旧的触发器
            mysqlEventHandlerInitialize.registerOrReplaceConsumer(ruleDomain);
        }
    }

    public void delEventOperation(EventOperationResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(
                eventRuleRepository.findById(dto.getRuleId()).orElse(null));
        //禁用事件规则
        ruleDomain.removeOperation(dto.getId());
        //使用新配置生成新的触发器替换旧的触发器
        mysqlEventHandlerInitialize.registerOrReplaceConsumer(ruleDomain);
    }


}
