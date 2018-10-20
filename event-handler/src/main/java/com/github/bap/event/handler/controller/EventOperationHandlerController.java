package com.github.bap.event.handler.controller;

import com.github.bap.event.handler.controller.dto.response.EventOperationResDTO;
import com.github.bap.event.handler.service.EventOperationService;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 周广
 **/
@RestController
@RequestMapping(value = "/eventOperation")
public class EventOperationHandlerController extends AbstractController {
    @Autowired
    private EventOperationService operationService;
    private static ScriptEngineManager SCRIPT_MANAGER = new ScriptEngineManager();

    /**
     * 校验脚本语法
     */
    @PostMapping(value = "/verifyScript")
    public ResultData verifyScript(@RequestBody EventOperationResDTO dto) {
        NashornScriptEngine scriptEngine = (NashornScriptEngine) SCRIPT_MANAGER.getEngineByName("nashorn");
        try {
            scriptEngine.eval(dto.getOperationScript());
        } catch (ScriptException e) {
            return ResultData.fail(ResultData.FAIL_CODE, e.getMessage());
        }
        return ResultData.success();
    }

    /**
     * 返回事件处理的js模板代码
     *
     * @return js脚本模板代码
     */
    @GetMapping
    public ResultData<String> getEventHandlerJsScriptTemplate() {
        String scriptTemplatePath = "static/event_handler.js";
        return generateScript(scriptTemplatePath);
    }

    @PostMapping
    public ResultData addOperation(@RequestBody EventOperationResDTO dto, HttpServletRequest request) {
        dto.setOperationScript(getEventHandlerJsScriptTemplate().getData());
        dto.setCreator(getLoginName(request));
        dto.setLastChangeUser(getLoginName(request));
        operationService.addEventOperation(dto);
        return ResultData.success();
    }

    @PutMapping
    public ResultData updateOperation(@RequestBody EventOperationResDTO dto, HttpServletRequest request) {
        dto.setLastChangeUser(getLoginName(request));
        operationService.updateEventOperation(dto);
        return ResultData.success();
    }

    @DeleteMapping
    public ResultData delOperation(@RequestBody EventOperationResDTO dto) {
        operationService.delEventOperation(dto);
        return ResultData.success();
    }


    private ResultData<String> generateScript(String scriptTemplatePath) {
        StringBuilder script = new StringBuilder();
        Resource resource = new ClassPathResource(scriptTemplatePath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String str;
            while ((str = reader.readLine()) != null) {
                script.append(str);
                script.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultData.generate(ResultData.FAIL_CODE, "脚本文件不存在", "");
        }
        return ResultData.success(script.toString());
    }

}
