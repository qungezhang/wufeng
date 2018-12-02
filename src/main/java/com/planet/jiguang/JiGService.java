package com.planet.jiguang;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @version:1.0.0
 * @Description:极光接口类
 * @author: aiveily
 * @date: 2018/10/31
 */
public class JiGService {

    private static final Logger logger = LoggerFactory.getLogger(JiGService.class);

    private static final String key = "7a35e4051e1e782ac858a6f7";

    private static final String secret = "e5b4baab53f076485e22a5bf";

    public static PushResult sendJiGMessage(Map<String,Object> map){
        try {
            JPushClient jPushClient = new JPushClient(secret, key);
            PushPayload p = PushPayload.alertAll(map.toString());
            return jPushClient.sendPush(p);
        } catch (APIConnectionException e) {
            e.printStackTrace();
            logger.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
        }
        return null;
    }
}
