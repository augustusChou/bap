package com.github.bap.event.handler.script.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.bap.common.exceptions.ServerException;
import com.github.bap.event.handler.dao.domain.EventFunctionConfigDomain;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * java提供给js调用的函数-钉钉
 *
 * @author 肖凯
 **/
public class DingDingFunction {


    /**
     * key是钉钉机器人名，value是对应的地址名
     */
    private static Map<String, String> TEMPLATE_MAP = new ConcurrentHashMap<>();
    private static Map<String, Integer> DATABASE_CONFIG_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 获取发送的钉钉的url
     *
     * @param dingDingName 数据库名称
     * @return jdbc操作模板
     */
    private static String getAccessTokenUrl(String dingDingName) {
        if (TEMPLATE_MAP.containsKey(dingDingName)) {
            return TEMPLATE_MAP.get(dingDingName);
        }

        synchronized (DingDingFunction.class) {
            if (TEMPLATE_MAP.containsKey(dingDingName)) {
                return TEMPLATE_MAP.get(dingDingName);
            }
            EventFunctionConfigDomain functionConfigDomain = FunctionBean
                    .getFunctionBean()
                    .getDomainFactory()
                    .createEventFunctionConfigDomain(EventFunctionConfigDomain.FunctionEnum.DINGTALK, dingDingName);
            String accessTokenUrl = functionConfigDomain.getAccessTokenUrl();
            TEMPLATE_MAP.put(dingDingName, accessTokenUrl);
            DATABASE_CONFIG_ID_MAP.put(dingDingName, functionConfigDomain.getConfigId());
            return accessTokenUrl;
        }
    }

    /**
     * 发送钉钉消息
     *
     * @param dingDingName 发送钉钉的名称
     * @param content      发送的消息
     * @return 发送结果
     */
    public static Object sendDingDing(String dingDingName, String content) {
        String tokenUrl = getAccessTokenUrl(dingDingName);
        //配置发钉钉的消息
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("msgtype", "text");
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("content", String.format("【%s】 %s", FunctionBean.getFunctionBean().getEnvEnum().getDesc(), content));
        postMap.put("text", contentMap);
        postMap.put("isAtAll", true);
        String postData = JSON.toJSONString(postMap);
        //发送钉钉
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(tokenUrl);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(postData, "utf-8");
        httppost.setEntity(se);
        HttpResponse response;
        String result = null;
        try {
            response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (IOException e) {
            throw new ServerException(dingDingName + "发送钉钉失败,异常:" + e);
        }
        log(dingDingName, content, null);
        return result;
    }

    private static void log(String dingDingName, String content, Object[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dingDingName", dingDingName);
        jsonObject.put("content", content);
        FunctionBean.getFunctionBean().logFunctionUse(
                DATABASE_CONFIG_ID_MAP.get(dingDingName), jsonObject.toJSONString()
        );
    }
}
