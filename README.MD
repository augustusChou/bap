#业务保障平台
###用途：
>订阅数据源、编写js脚本检测数据变动来执行修复、报警操作。目前已经实现canal数据源


###canal数据源使用流程：
初始化数据库脚本

设置event-handler项目下的resources/config/xxx/application.properties配置文件的canal配置。
>canal.ip=127.0.0.1

>canal.port=11111

使用npm编译运行web/ 使用账号:test  密码:test登录

事件功能配置是用于添加在js脚本用使用的功能的配置，假如你在这设置一个MYSQL数据库配置，在js脚本就能对这个数据库执行sql

事件处理配置是关键、一个表可以创建三个事件规则（增、删、改），点击新增新增事件处理创建一个规则。
>数据库名称、表名、查询类型是组合索引不可重复。

一个事件规则可以对应多个事件操作、事件操作有3个属性：备注、延迟执行时间、脚本代码
>备注：用于描述这个操作是干嘛的，不可以超过256字符

>延迟执行时间：执行完check_event后，返回true 准备执行run的之前的等待时间

>脚本代码：js脚本为了方便设计了3个函数(check_event、run、repair) 
从check_event开始执行，返回不为 'ok' 才执行run， run执行完返回不为 'ok' 执行repair

然后就可以去修改数据库数据测试js执行结果
>在db_event_operation_log可以看到操作的执行结果

>在db_event_func_use_log可以看到操作在执行过程中使用的功能（sql执行、redis、mq、钉钉等）