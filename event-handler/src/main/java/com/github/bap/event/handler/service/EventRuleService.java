package com.github.bap.event.handler.service;

import com.github.bap.event.handler.controller.dto.request.EventRuleReqDTO;
import com.github.bap.event.handler.controller.dto.response.EventOperationLogResDTO;
import com.github.bap.event.handler.controller.dto.response.EventOperationResDTO;
import com.github.bap.event.handler.controller.dto.response.EventRuleResDTO;
import com.github.bap.event.handler.controller.dto.response.PageResponse;
import com.github.bap.event.handler.core.MysqlEventHandlerInitialize;
import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.domain.EventRuleDomain;
import com.github.bap.event.handler.dao.po.DbEventOperationLogPO;
import com.github.bap.event.handler.dao.po.DbEventRulePO;
import com.github.bap.event.handler.dao.repository.EventOperationLogRepository;
import com.github.bap.event.handler.dao.repository.EventOperationRepository;
import com.github.bap.event.handler.dao.repository.EventRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 周广
 **/
@Service
public class EventRuleService {
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


    public PageResponse<EventRuleResDTO> getEventRule(EventRuleReqDTO dto) {
        //构建一个条件查询
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        DbEventRulePO exampleParam = createQueryExample(dto);
        Example<DbEventRulePO> example = Example.of(exampleParam, matcher);

        Page<DbEventRulePO> page = eventRuleRepository.findAll(
                example,
                PageResponse.createPageable(dto.getPageNo(), dto.getPageSize()));

        return PageResponse.of(page.getTotalElements(), convertResultObject(page));
    }

    private List<EventRuleResDTO> convertResultObject(Page<DbEventRulePO> page) {
        List<EventRuleResDTO> resDTOList = page.getContent().stream()
                .map(EventRuleResDTO::convert).collect(Collectors.toList());
        for (EventRuleResDTO resDTO : resDTOList) {
            resDTO.setOperationList(eventOperationRepository.findAllByRuleId(resDTO.getId())
                    .stream().map(EventOperationResDTO::convert).collect(Collectors.toList()));
        }
        return resDTOList;
    }

    private DbEventRulePO createQueryExample(EventRuleReqDTO dto) {
        DbEventRulePO exampleParam = new DbEventRulePO();
        exampleParam.settName(dto.gettName());
        exampleParam.setSchemasName(dto.getSchemasName());
        exampleParam.setEventType(dto.getEventType());
        return exampleParam;
    }


    public void addEventRule(EventRuleResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(dto);
        //将rule存储到数据库
        ruleDomain.saveEventRule();
        //使用新配置生成新的触发器替换旧的触发器
        mysqlEventHandlerInitialize.registerOrReplaceConsumer(ruleDomain);
    }

    public void updateEventRule(EventRuleResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(dto);
        //将rule存储到数据库
        ruleDomain.saveEventRule();
        //使用新配置生成新的触发器替换旧的触发器
        mysqlEventHandlerInitialize.registerOrReplaceConsumer(ruleDomain);
    }

    public void delEventRule(EventRuleResDTO dto) {
        EventRuleDomain ruleDomain = domainFactory.createEventRuleDomain(dto);
        //移除注册的事件消费者
        mysqlEventHandlerInitialize.removeConsumer(String.valueOf(dto.getId()));
        //禁用事件规则
        ruleDomain.disableEventRule();
    }


    public PageResponse<EventOperationLogResDTO> getOperationLog(int pageNo, int pageSize, Integer operationId) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        DbEventOperationLogPO exampleParam = createOperationLogQueryExample(operationId);
        Example<DbEventOperationLogPO> example = Example.of(exampleParam, matcher);

        Page<DbEventOperationLogPO> page = eventOperationLogRepository.findAll(
                example,
                PageResponse.createPageable(pageNo, pageSize));
        return PageResponse.of(
                page.getTotalElements(),
                page.getContent().stream().map(EventOperationLogResDTO::convert).collect(Collectors.toList()));
    }

    private DbEventOperationLogPO createOperationLogQueryExample(Integer operationId) {
        DbEventOperationLogPO exampleParam = new DbEventOperationLogPO();
        exampleParam.setOperationId(operationId);
        return exampleParam;
    }


}
