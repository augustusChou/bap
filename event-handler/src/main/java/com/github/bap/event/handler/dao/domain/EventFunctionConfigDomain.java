package com.github.bap.event.handler.dao.domain;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.controller.dto.response.FuncConfigInfoDTO;
import com.github.bap.event.handler.dao.po.DbEventFuncConfigPO;
import com.github.bap.event.handler.dao.repository.EventFuncConfigRepository;
import lombok.Data;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Timestamp;

/**
 * @author 周广
 **/
public class EventFunctionConfigDomain {

    private FunctionEnum functionEnum;
    private String configName;
    private EventFuncConfigRepository eventFuncConfigRepository;
    private DbEventFuncConfigPO config;

    public EventFunctionConfigDomain(FunctionEnum functionEnum, String configName,
                                     EventFuncConfigRepository eventFuncConfigRepository) {
        this.functionEnum = functionEnum;
        this.configName = configName;
        this.eventFuncConfigRepository = eventFuncConfigRepository;
        this.config = eventFuncConfigRepository.findByConfigTypeAndConfigName(
                this.functionEnum.getType(), this.configName);
    }


    public EventFunctionConfigDomain(FuncConfigInfoDTO configInfoDTO,
                                     EventFuncConfigRepository eventFuncConfigRepository) {
        this.functionEnum = FunctionEnum.convent(configInfoDTO.getConfigType());
        this.configName = configInfoDTO.getConfigName();
        this.eventFuncConfigRepository = eventFuncConfigRepository;

        if (configInfoDTO.getId() == null) {
            this.config = new DbEventFuncConfigPO();
            this.config.setCreateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            this.config = eventFuncConfigRepository.findById(configInfoDTO.getId()).orElse(null);
            if (this.config == null) {
                throw new ServerException("功能配置id错误");
            }
            this.config.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        }

        this.config.setConfigType(this.functionEnum.getType());
        this.config.setConfigName(this.configName);
        this.config.setConfigInfo(configInfoDTO.getConfigInfo());
    }

    public void remove() {
        if (this.config != null && this.config.getId() != null) {
            this.eventFuncConfigRepository.deleteById(this.config.getId());
        }
    }

    public void save() {
        this.eventFuncConfigRepository.save(this.config);
    }

    public Integer getConfigId() {
        return this.config.getId();
    }


    /**
     * 获取数据库对应的jdbc操作模板
     *
     * @return jdbc操作模板
     */
    public JdbcTemplate getJdbcTemplate() {
        if (this.config == null) {
            throw new ServerException(this.configName + "数据库配置不存在");
        }
        MysqlConfig mysqlConfig = JSON.parseObject(config.getConfigInfo(), MysqlConfig.class);
        return mysqlConfig.createJdbcTemplate();
    }

    /**
     * 获取发送钉钉的URL
     *
     * @return 发送钉钉的url
     */
    public String getAccessTokenUrl() {
        if (this.config == null) {
            throw new ServerException(this.configName + "钉钉配置不存在");
        }
        DingDingConfig dingDingConfig = JSON.parseObject(config.getConfigInfo(), DingDingConfig.class);
        return dingDingConfig.getAccessTokenUrl();
    }

    /**
     * 获取发送mq实例对象
     *
     * @return 返回mq发送实例对象
     */
    public DefaultMQProducer getProducer() throws Exception  {
        if (this.config == null) {
            throw new ServerException(this.configName + "MQ配置不存在");
        }
        SendMqConfig sendMqConfig = JSON.parseObject(config.getConfigInfo(), SendMqConfig.class);
        return sendMqConfig.getProducer();

    }

    /**
     * 获取缓存对象模板
     *
     * @return 返回缓存对象模板对象
     */
    public StringRedisTemplate getRedisTemplate() {
        if (this.config == null) {
            throw new ServerException(this.configName + "Redis配置不存在");
        }
        RedisConfig redisConfig = JSON.parseObject(config.getConfigInfo(), RedisConfig.class);

        return redisConfig.getRedisTemplate();

    }


    public enum FunctionEnum {

        /**
         * 数据库
         */
        MYSQL_DATABASE("0", "MYSQL数据库"),

        /**
         * redis 缓存
         */
        REDIS("1", "Redis数据库"),

        /**
         * 钉钉
         */
        DINGTALK("2", "阿里钉钉"),
        /**
         * 发送MQ
         */
        SEND_MQ("3", "发送的MQ");

        private String type;
        private String desc;

        FunctionEnum(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public static FunctionEnum convent(String type) {
            for (FunctionEnum value : values()) {
                if (value.type.equals(type)) {
                    return value;
                }
            }
            throw new ServerException("配置类型不支持");
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

    @Data
    public static class MysqlConfig {

        private String url;
        private String username;
        private String password;
        /**
         * 连接池初始化大小
         */
        private Integer initializeSize;
        /**
         * 连接池最小
         */
        private Integer minIdle;
        /**
         * 连接池最大
         */
        private Integer maxActive;


        JdbcTemplate createJdbcTemplate() {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            //配置初始化大小、最小、最大
            dataSource.setInitialSize(initializeSize == null ? 1 : initializeSize);
            dataSource.setMinIdle(minIdle == null ? 1 : minIdle);
            dataSource.setMaxActive(maxActive == null ? 20 : maxActive);
            //配置获取连接等待超时的时间
            dataSource.setMaxWait(6000);
            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setValidationQuery("SELECT 'x'");

            return new JdbcTemplate(dataSource);
        }
    }

    @Data
    public static class DingDingConfig {
        private String url;
        private String accessToken;

        String getAccessTokenUrl() {
            String accessTokenUrl = url + "?access_token=" + accessToken;
            return accessTokenUrl;
        }
    }

    @Data
    public static class SendMqConfig {
        private String producerId;
        private String nameSrvAddress;

        DefaultMQProducer getProducer() throws Exception  {
            DefaultMQProducer producer=new DefaultMQProducer(producerId);
            producer.setNamesrvAddr(nameSrvAddress);
            producer.start();
            return producer;
        }
    }

    @Data
    public static class RedisConfig {
        private String host;
        private int port;
        private String password;
        private int poolMaxActive;
        private Long poolMaxWait;
        private int poolMaxIdle;
        private int poolMinIdle;
        private int timeout;

        StringRedisTemplate getRedisTemplate() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            //最大连接数
            jedisPoolConfig.setMaxTotal(poolMaxActive);
            // 连接池中的最大空闲连接
            jedisPoolConfig.setMaxIdle(poolMaxIdle);
            //最小空闲连接数
            jedisPoolConfig.setMinIdle(poolMinIdle);
            //当池内没有可用的连接时，最大等待时间
            jedisPoolConfig.setMaxWaitMillis(poolMaxWait);

            RedisStandaloneConfiguration redisStandaloneConfiguration =
                    new RedisStandaloneConfiguration();
            //设置redis服务器的host或者ip地址
            redisStandaloneConfiguration.setHostName(host);
            //设置默认使用的数据库
            //redisStandaloneConfiguration.setDatabase(database);
            //设置密码
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
            //设置redis的服务的端口号
            redisStandaloneConfiguration.setPort(port);
            //获得默认的连接池构造器
            JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                    (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
            //指定jedisPoolConifig来修改默认的连接池构造器
            jpcb.poolConfig(jedisPoolConfig);
            //通过构造器来构造jedis客户端配置
            JedisClientConfiguration jedisClientConfiguration = jpcb.build();
            //单机配置 + 客户端配置 = jedis连接工厂
            JedisConnectionFactory jedisConnectionFactory =
                    new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
            return new StringRedisTemplate(jedisConnectionFactory);
        }
    }

}
