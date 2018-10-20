package com.github.bap.event.handler.controller;

import com.github.bap.event.handler.controller.dto.response.FuncConfigInfoDTO;
import com.github.bap.event.handler.controller.dto.response.FuncConfigResDTO;
import com.github.bap.event.handler.controller.dto.response.FuncListResDTO;
import com.github.bap.event.handler.controller.dto.response.PageResponse;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import com.github.bap.event.handler.service.FunctionConfigService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 周广
 **/
@RestController
@RequestMapping(value = "funcConfig")
public class FunctionConfigController {
    @Autowired
    private FunctionConfigService functionConfigService;


    @GetMapping
    public ResultData<FuncConfigResDTO> getFuncConfigList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        PageResponse<FuncConfigInfoDTO> pageResponse = functionConfigService.getFuncConfigList(pageNo, pageSize);
        List<FuncListResDTO> funcListResList = createFuncList();
        return ResultData.success(new FuncConfigResDTO(pageResponse, funcListResList));
    }

    @PostMapping
    public ResultData addFuncConfig(@RequestBody FuncConfigInfoDTO dto) {
        functionConfigService.addOrUpdateFuncConfig(dto);
        return ResultData.success();
    }


    @PutMapping
    public ResultData updateFuncConfig(@RequestBody FuncConfigInfoDTO dto) {
        functionConfigService.addOrUpdateFuncConfig(dto);
        return ResultData.success();
    }

    @DeleteMapping
    public ResultData delFuncConfig(@RequestBody FuncConfigInfoDTO dto) {
        functionConfigService.delFuncConfig(dto);
        return ResultData.success();
    }


    private List<FuncListResDTO> createFuncList() {
        List<FuncListResDTO> funcListResList = Lists.newArrayList();
        for (EventFunctionConfigDomain.FunctionEnum value : EventFunctionConfigDomain.FunctionEnum.values()) {
            funcListResList.add(FuncListResDTO.convert(value));
        }
        return funcListResList;
    }
}
