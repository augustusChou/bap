package com.github.bap.event.handler.trigger.mysql;

import com.github.bap.event.handler.trigger.LoggingHandler;
import com.github.bap.event.handler.trigger.Operation;
import com.github.bap.event.handler.script.OperationContext;
import com.github.bap.event.handler.script.ScriptOperation;
import com.github.bap.event.handler.script.func.FunctionBean;
import com.github.bap.event.source.Event;
import com.github.bap.mysql.event.source.CanalEventInfo;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 周广
 **/
@Slf4j
public class MysqlOperation implements Operation<CanalEventInfo>, ScriptOperation {


    private static ScriptEngineManager SCRIPT_MANAGER = new ScriptEngineManager();
    private static final String OPERATION_CODE = "ok";
    private LoggingHandler<CanalEventInfo> logHandler;
    private String operationId;
    private OperationContext context;
    private int delayTime;
    private String script;
    /**
     * 用于执行run函数和repair函数的线程池
     */
    private ScheduledExecutorService timer;
    /**
     * 脚本执行引擎
     */
    private NashornScriptEngine scriptEngine;

    public MysqlOperation(String operationId, String script, ScheduledExecutorService operationExecPool) throws ScriptException {
        this(operationId, script, operationExecPool, 0);
    }


    /**
     * 构造一个Mysql 操作对象
     *
     * @param operationId       操作id 用于区分不同操作
     * @param script            js脚本代码
     * @param operationExecPool 脚本执行使用的线程池
     * @param delayTime         脚本延迟执行时间（check_event通过后延迟执行run的时间）
     * @throws ScriptException js脚本执行出错抛出
     */
    public MysqlOperation(String operationId, String script, ScheduledExecutorService operationExecPool, int delayTime) throws ScriptException {
        this.operationId = operationId;
        this.context = new OperationContext(operationId);
        this.delayTime = delayTime;
        this.script = script;
        this.timer = operationExecPool;
        //使用nashorn脚本引擎
        this.scriptEngine = (NashornScriptEngine) SCRIPT_MANAGER.getEngineByName("nashorn");
        this.scriptEngine.eval(script);
    }


    @Override
    public String getOperationId() {
        return operationId;
    }

    @Override
    public void executor(Event<CanalEventInfo> event) throws ScriptException, NoSuchMethodException {

        //检查是否为自己需要的事件
        Boolean checkResult = (Boolean) this.scriptEngine.invokeFunction("check_event", event.getData());
        if (checkResult) {
            log.info(String.format("添加任务(operationId:%s)到线程池", this.operationId));
            timer.schedule(new ScriptExecutorThread(event), this.delayTime, TimeUnit.MILLISECONDS);
        }

    }

    @Override
    public void setLogHandler(LoggingHandler<CanalEventInfo> logHandler) {
        this.logHandler = logHandler;
    }

    @Override
    public void replaceScript(String script) throws ScriptException {
        this.scriptEngine.eval(script);
    }


    class ScriptExecutorThread implements Runnable {
        private Event<CanalEventInfo> event;

        ScriptExecutorThread(Event<CanalEventInfo> event) {
            this.event = event;
        }

        @Override
        public void run() {
            //设置当前操作上下文变量
            FunctionBean.OPERATION_ID.set(context);
            String runResult = "";
            String repairResult = "";
            Throwable throwable = null;
            try {
                //执行事件处理
                runResult = String.valueOf(scriptEngine.invokeFunction("run", this.event.getData()));

                //执行结果不ok才进行后续处理
                if (!OPERATION_CODE.equals(runResult)) {
                    //调用修正逻辑
                    repairResult = String.valueOf(scriptEngine.invokeFunction("repair", this.event.getData()));
                    //记录日志,必须有日志处理器
                }
            } catch (Throwable e) {
                throwable = e;
            } finally {
                if (logHandler != null) {
                    LogInfo logInfo = new LogInfo(runResult, repairResult, throwable);
                    logHandler.log(this.event, logInfo);
                }
            }
        }
    }

    @Data
    public static class LogInfo {
        /**
         * 执行函数结果
         */
        private String runResult;
        /**
         * 修复函数结果
         */
        private String repairResult;

        /**
         * 脚本执行异常
         */
        private Throwable scriptExecException;

        LogInfo(String runResult, String repairResult) {
            this.runResult = runResult;
            this.repairResult = repairResult;
        }

        public LogInfo(String runResult, String repairResult, Throwable scriptExecException) {
            this.runResult = runResult;
            this.repairResult = repairResult;
            this.scriptExecException = scriptExecException;
        }

        public boolean existsException() {
            return scriptExecException != null;
        }
    }
}
