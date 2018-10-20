package com.github.bap.event.handler.script;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author 周广
 **/
public class OperationContextTest {

    @Test
    public void setParam() {
        String operationId = "1";
        String testKey = "key";
        String testValue = "value";
        OperationContext context = new OperationContext(operationId);
        context.setParam(testKey, testValue);
        assertEquals(context.getParam(testKey), testValue);
    }

    @Test
    public void getOperationId() {
        String operationId = "1";
        OperationContext context = new OperationContext(operationId);
        assertEquals(operationId, context.getOperationId());
    }
}