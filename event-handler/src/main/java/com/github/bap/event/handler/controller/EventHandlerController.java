package com.github.bap.event.handler.controller;

import com.github.bap.event.handler.controller.dto.response.PageResponse;
import com.github.bap.event.handler.controller.dto.request.EventRuleReqDTO;
import com.github.bap.event.handler.controller.dto.response.EventRuleResDTO;
import com.github.bap.event.handler.service.EventRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 周广
 **/
@RestController
@RequestMapping(value = "/eventRule")
public class EventHandlerController extends AbstractController {
    @Autowired
    private EventRuleService eventRuleService;


    @GetMapping
    public ResultData<PageResponse<EventRuleResDTO>> getEventRule(EventRuleReqDTO dto) {
        return ResultData.success(eventRuleService.getEventRule(dto));
    }

    @PostMapping
    public ResultData addEventRule(@RequestBody EventRuleResDTO dto, HttpServletRequest request) {
        dto.setCreator(getLoginName(request));
        dto.setLastChangeUser(getLoginName(request));
        eventRuleService.addEventRule(dto);
        return ResultData.success();
    }

    @PutMapping
    public ResultData updateEventRule(@RequestBody EventRuleResDTO dto, HttpServletRequest request) {
        dto.setLastChangeUser(getLoginName(request));
        eventRuleService.updateEventRule(dto);
        return ResultData.success();
    }

    @DeleteMapping
    public ResultData delEventRule(@RequestBody EventRuleResDTO dto, HttpServletRequest request) {
        dto.setLastChangeUser(getLoginName(request));
        eventRuleService.delEventRule(dto);
        return ResultData.success();
    }
}
