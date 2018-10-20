package com.github.bap.event.handler.script;

import javax.script.ScriptException;

/**
 * @author 周广
 **/
public interface ScriptOperation {


    /**
     * 替换脚本
     *
     * @param script 脚本代码
     */
    void replaceScript(String script) throws Exception;

}
