package com.github.bap.event.handler.script;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周广
 **/
public class OperationContext {

    private String operationId;
    private Map<String, String> param;

    public OperationContext(String operationId) {
        this.operationId = operationId;
        this.param = new ConcurrentHashMap<>();
    }


    public void setParam(String key, String value) {
        this.param.put(key, value);
    }

    public String getParam(String key) {
        return this.param.get(key);
    }

    public String getOperationId() {
        return operationId;
    }
}
