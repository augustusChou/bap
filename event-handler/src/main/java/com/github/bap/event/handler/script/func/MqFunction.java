package com.github.bap.event.handler.script.func;

import com.alibaba.fastjson.JSONObject;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * java提供给js调用的函数-发送mq
 *
 * @author 肖凯
 **/
public class MqFunction {


    /**
     * key是MQ名称
     */
    private static Map<String, DefaultMQProducer> SEND_MQ_MAP = new ConcurrentHashMap<>();
    private static Map<String, Integer> DATABASE_CONFIG_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 创建发送的消息MQ生产者
     *
     * @param sendMqName 发送的mq名称
     * @return mq操作模板
     */
    private static DefaultMQProducer createProducer(String sendMqName) throws Exception {
        if (SEND_MQ_MAP.containsKey(sendMqName)) {
            return SEND_MQ_MAP.get(sendMqName);
        }

        synchronized (MqFunction.class) {
            if (SEND_MQ_MAP.containsKey(sendMqName)) {
                return SEND_MQ_MAP.get(sendMqName);
            }
            EventFunctionConfigDomain functionConfigDomain = FunctionBean
                    .getFunctionBean()
                    .getDomainFactory()
                    .createEventFunctionConfigDomain(EventFunctionConfigDomain.FunctionEnum.SEND_MQ, sendMqName);
            DefaultMQProducer producer = functionConfigDomain.getProducer();
            SEND_MQ_MAP.put(sendMqName, producer);
            DATABASE_CONFIG_ID_MAP.put(sendMqName, functionConfigDomain.getConfigId());
            return producer;
        }
    }
    /**
     * 单边发送
     *
     * @param topic   topic
     * @param tag     tag
     * @param content 字符串消息体
     */
    public void oneWaySender(String sendMqName ,String topic, String tag, String content) {
        try {
            DefaultMQProducer   producer= createProducer(sendMqName);
            Message msg =new Message(topic, tag, content.getBytes());
            producer.sendOneway(msg);
            log( sendMqName,  topic,  tag,  content,  "SUCCESS");
        } catch (Exception e) {
            log( sendMqName,  topic,  tag,  content,  "FAIL");
        }
    }

    private static void log(String sendMqName, String topic, String tag, String content, String messegeType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("databaseName", sendMqName);
        jsonObject.put("topic", topic);
        jsonObject.put("content", content);
        jsonObject.put("tag", tag);
        jsonObject.put("messegeType", messegeType);
        FunctionBean.getFunctionBean().logFunctionUse(DATABASE_CONFIG_ID_MAP.get(sendMqName), jsonObject.toJSONString());
    }

}
