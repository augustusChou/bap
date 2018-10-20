var MYSQL = Java.type('com.github.bap.event.handler.script.func.MysqlFunction');
var DINGDING = Java.type('com.github.bap.event.handler.script.func.DingDingFunction');
var REDIS = Java.type('com.github.bap.event.handler.script.func.RedisFunction');
var SENDMQ = Java.type('com.github.bap.event.handler.script.func.MqFunction');

/**
 * 检查事件是否为自己需要
 * @param event 事件信息
 * @return boolean 返回是否为自己需要的事件
 */
function check_event(event) {
    //在这里输入js代码

    return false;
}

/**
 * 执行事件处理
 * @param event 事件信息
 * @return string 执行状态 'ok':成功
 */
function run(event) {
    //在这里输入js代码


    return 'ok';
}

/**
 * 执行后续修正逻辑，比如报警或者修复数据
 * 只有在run函数返回不为ok的时候执行
 * @param event 事件信息
 * @return string 执行状态会被记录到日志 'ok':成功
 */
function repair(event) {
    //在这里输入js代码 可选

    return 'ok';
}