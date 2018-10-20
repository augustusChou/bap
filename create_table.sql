CREATE DATABASE `bap` DEFAULT CHARACTER SET utf8;

create table if not exists db_event_func_config
(
	id int auto_increment
		primary key,
	config_type char default '' not null comment '0:数据库 1:redis 2:钉钉报警',
	config_name varchar(64) default '' not null comment '配置名称，比如数据库就是数据库名称。redis就是0-15这种数据库名称',
	config_info text not null comment '配置信息',
	create_time datetime not null,
	update_time timestamp default CURRENT_TIMESTAMP not null,
	constraint db_event_func_config_config_type_config_name_uindex
		unique (config_type, config_name)
)
comment '事件提供的功能的配置表'
;

create table if not exists db_event_func_use_log
(
	id int auto_increment
		primary key,
	operation_id int not null comment '操作id',
	config_id int not null comment '使用的配置id',
	config_param text null comment '使用的配置的调用参数',
	create_time datetime not null
)
comment '函数功能使用记录'
;

create table if not exists db_event_operation
(
	id int auto_increment
		primary key,
	rule_id int not null comment 'db_event_rule的主键',
	operation_script text null,
	create_time datetime not null,
	update_time timestamp default CURRENT_TIMESTAMP not null,
	remarks varchar(256) default '' not null comment '操作的描述',
	creator varchar(64) default '' null comment '创建者',
	last_change_user varchar(64) default '' null comment '最后修改人',
	delay_time int default '0' null comment '延迟执行时间，单位为毫秒'
)
comment '数据库事件操作'
;

create index db_event_operation_rule_id_index
	on db_event_operation (rule_id)
;

create table if not exists db_event_operation_log
(
	id int auto_increment
		primary key,
	operation_id int not null comment 'db_event_operation 主键',
	event_data text not null comment '事件数据',
	run_result varchar(512) default '' not null comment '执行函数结果信息',
	repair_result varchar(512) default '' null comment '修复函数结果',
	exception_info text null comment '异常信息',
	create_time datetime not null
)
comment '事件操作日志'
;

create table if not exists db_event_rule
(
	id int auto_increment
		primary key,
	schemas_name varchar(32) charset latin1 default '' not null comment '数据库名称',
	t_name varchar(64) charset latin1 default '' null comment '表名',
	event_type int default '0' null comment '0:查 1:增 2:改 3:删',
	create_time datetime not null,
	update_time timestamp default CURRENT_TIMESTAMP not null,
	rule_status char default '0' not null comment '1是启用 0是禁用',
	creator varchar(64) default '' null comment '创建者',
	last_change_user varchar(64) default '' not null comment '最后修改人',
	constraint db_event_rule_s_n_t_e_uindex
		unique (schemas_name, t_name, event_type)
)
comment '数据库事件匹配规则'
;

create table if not exists operation_user
(
	id int auto_increment
		primary key,
	user_name varchar(64) default '' not null,
	login_name varchar(64) default '' not null,
	login_password varchar(256) default '' not null,
	create_time datetime not null,
	update_time timestamp default CURRENT_TIMESTAMP not null,
	constraint operation_user_login_name_uindex
		unique (login_name)
)
comment '用户表'
;

