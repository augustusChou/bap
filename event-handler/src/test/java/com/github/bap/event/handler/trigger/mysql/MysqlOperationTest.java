package com.github.bap.event.handler.trigger.mysql;

import com.github.bap.mysql.event.source.CanalEventInfo;
import com.github.bap.mysql.event.source.MysqlEvent;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author 周广
 **/
public class MysqlOperationTest {
    private String operationId = "operationId";
    private String script = "var MYSQL = Java.type('com.github.bap.event.handler.script.func.MysqlFunction');\n" +
            "\n" +
            "/**\n" +
            " * 检查事件是否为自己需要\n" +
            " * @param event 事件信息\n" +
            " * @return boolean 返回是否为自己需要的事件\n" +
            " */\n" +
            "function check_event(event) {\n" +
            "    //在这里输入js代码\n" +
            "\n" +
            "    return true;\n" +
            "}\n" +
            "\n" +
            "/**\n" +
            " * 执行事件处理\n" +
            " * @param event 事件信息\n" +
            " * @return string 执行状态 'ok':成功(不ok会被记录到日志)\n" +
            " */\n" +
            "function run(event) {\n" +
            "    //在这里输入js代码\n" +
            "\n" +
            "\n" +
            "    return 'fail';\n" +
            "}\n" +
            "\n" +
            "/**\n" +
            " * 执行后续修正逻辑，比如报警或者修复数据\n" +
            " * 只有在run函数返回不为ok的时候执行\n" +
            " * @param event 事件信息\n" +
            " * @return string 执行状态 'ok':成功(会被记录到日志)\n" +
            " */\n" +
            "function repair(event) {\n" +
            "    //在这里输入js代码 可选\n" +
            "\n" +
            "    return '修复失败';\n" +
            "}";
    private MysqlOperation operation;

    @Before
    public void setUp() throws Exception {
        operation = new MysqlOperation(operationId, script, new ScheduledThreadPoolExecutor(
                1, new DefaultThreadFactory("Script-Exec-ThreadPool")));
    }

    @Test
    public void executor() throws ScriptException, NoSuchMethodException {

        Assert.assertEquals(operation.getOperationId(), operationId);
        operation.setLogHandler((event, logData) -> {
            MysqlOperation.LogInfo logInfo = (MysqlOperation.LogInfo) logData;
            Assert.assertEquals(logInfo.getRunResult(), "fail");
            Assert.assertEquals(logInfo.getRepairResult(), "修复失败");
        });
        operation.executor(new MysqlEvent(new CanalEventInfo()));
    }


    @Test
    public void getOperationIdTest() {
        Assert.assertEquals(operation.getOperationId(), operationId);
    }

    @Test
    public void replaceScriptTest() throws ScriptException, NoSuchMethodException {
        this.operation.replaceScript("var MYSQL = Java.type('com.github.bap.event.handler.script.func.MysqlFunction');\n" +
                "\n" +
                "/**\n" +
                " * 检查事件是否为自己需要\n" +
                " * @param event 事件信息\n" +
                " * @return boolean 返回是否为自己需要的事件\n" +
                " */\n" +
                "function check_event(event) {\n" +
                "    //在这里输入js代码\n" +
                "\n" +
                "    return true;\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 执行事件处理\n" +
                " * @param event 事件信息\n" +
                " * @return string 执行状态 'ok':成功(不ok会被记录到日志)\n" +
                " */\n" +
                "function run(event) {\n" +
                "    //在这里输入js代码\n" +
                "\n" +
                "\n" +
                "    return 'ok';\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * 执行后续修正逻辑，比如报警或者修复数据\n" +
                " * 只有在run函数返回不为ok的时候执行\n" +
                " * @param event 事件信息\n" +
                " * @return string 执行状态 'ok':成功(会被记录到日志)\n" +
                " */\n" +
                "function repair(event) {\n" +
                "    //在这里输入js代码 可选\n" +
                "\n" +
                "    return '修复失败';\n" +
                "}");
        operation.setLogHandler((event, logData) -> {
            MysqlOperation.LogInfo logInfo = (MysqlOperation.LogInfo) logData;
            Assert.assertEquals(logInfo.getRunResult(), "ok");
            Assert.assertEquals(logInfo.getRepairResult(), "");
        });
        operation.executor(new MysqlEvent(new CanalEventInfo()));
    }


}