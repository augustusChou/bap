package com.github.bap.event.handler.script.func;

import com.alibaba.fastjson.JSONObject;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * java提供给js调用的函数-缓存
 *
 * @author 肖凯
 **/
public class RedisFunction {


    /**
     * key是数据库名称
     */
    private static Map<String, StringRedisTemplate> REDIS_MAP = new ConcurrentHashMap<>();
    private static Map<String, Integer> DATABASE_CONFIG_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 获取缓存连接对象
     *
     * @param redisName 缓存对象名称
     * @return 缓存模板对象
     */
    private static StringRedisTemplate getRedisTemplate(String redisName) {
        if (REDIS_MAP.containsKey(redisName)) {
            return REDIS_MAP.get(redisName);
        }
        synchronized (RedisFunction.class) {
            if (REDIS_MAP.containsKey(redisName)) {
                return REDIS_MAP.get(redisName);
            }
            EventFunctionConfigDomain functionConfigDomain = FunctionBean
                    .getFunctionBean()
                    .getDomainFactory()
                    .createEventFunctionConfigDomain(EventFunctionConfigDomain.FunctionEnum.REDIS, redisName);
            StringRedisTemplate redisTemplate = functionConfigDomain.getRedisTemplate();
            REDIS_MAP.put(redisName, redisTemplate);
            DATABASE_CONFIG_ID_MAP.put(redisName, functionConfigDomain.getConfigId());
            return redisTemplate;
        }
    }

    /**
     * 设置操作字符串
     *
     * @param redisName 缓存对象名称
     * @param key       键
     * @param value     值
     * @param timeOut   有效时间
     */
    public static Object setForValue(String redisName, String key, String value, int timeOut) {
        StringRedisTemplate stringRedisTemplate = getRedisTemplate(redisName);
        log(redisName, "set", null);
        if (timeOut == 0) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
        }
        return true;

    }

    /**
     * 获得操作字符串
     *
     * @param redisName 缓存对象名称
     * @param key       键
     */
    public static Object getForValue(String redisName, String key) {
        StringRedisTemplate stringRedisTemplate = getRedisTemplate(redisName);
        log(redisName, "get", null);
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除操作字符串
     *
     * @param redisName 缓存对象名称
     * @param key       键
     */
    public static Object deleteForValue(String redisName, String key) {
        StringRedisTemplate stringRedisTemplate = getRedisTemplate(redisName);
        log(redisName, "delete", null);
        stringRedisTemplate.delete(key);
        return true;
    }

    private static void log(String redisName, String type, Object[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("redisName", redisName);
        jsonObject.put("type", type);
        FunctionBean.getFunctionBean().logFunctionUse(DATABASE_CONFIG_ID_MAP.get(redisName), jsonObject.toJSONString());
    }
}
