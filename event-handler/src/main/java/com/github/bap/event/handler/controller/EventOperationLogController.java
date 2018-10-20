package com.github.bap.event.handler.controller;

import com.github.bap.event.handler.controller.dto.response.PageResponse;
import com.github.bap.event.handler.controller.dto.response.EventOperationLogResDTO;
import com.github.bap.event.handler.service.EventRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 周广
 **/
@RestController
@RequestMapping(value = "eventOperationLog")
public class EventOperationLogController {
    @Autowired
    private EventRuleService eventRuleService;

    @GetMapping
    public ResultData<PageResponse<EventOperationLogResDTO>> getEventRule(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                          @RequestParam(value = "operationId") Integer operationId) {
        return ResultData.success(eventRuleService.getOperationLog(pageNo, pageSize, operationId));
    }

}
