package com.github.bap.event.handler.service;

import com.github.bap.event.handler.controller.dto.response.FuncConfigInfoDTO;
import com.github.bap.event.handler.controller.dto.response.PageResponse;
import com.github.bap.event.handler.dao.domain.DomainFactory;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import com.github.bap.event.handler.dao.po.DbEventFuncConfigPO;
import com.github.bap.event.handler.dao.repository.EventFuncConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author 周广
 **/
@Service
public class FunctionConfigService {
    @Autowired
    private EventFuncConfigRepository eventFuncConfigRepository;
    @Autowired
    private DomainFactory domainFactory;

    public PageResponse<FuncConfigInfoDTO> getFuncConfigList(int pageNo, int pageSize) {
        Page<DbEventFuncConfigPO> page = eventFuncConfigRepository.findAll(PageResponse.createPageable(pageNo, pageSize));
        return PageResponse.of(
                page.getTotalElements(),
                page.getContent().stream().map(FuncConfigInfoDTO::convert).collect(Collectors.toList()));
    }

    public void addOrUpdateFuncConfig(FuncConfigInfoDTO dto) {
        EventFunctionConfigDomain functionConfigDomain = domainFactory.createEventFunctionConfigDomain(dto);
        functionConfigDomain.save();
    }


    public void delFuncConfig(FuncConfigInfoDTO dto) {
        EventFunctionConfigDomain functionConfigDomain = domainFactory.createEventFunctionConfigDomain(dto);
        functionConfigDomain.remove();
    }


}
