package com.spicy.wechat;

import com.fasterxml.jackson.databind.JsonNode;
import com.spicy.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class WeChatResult {

    public static <T> T processWeChatResult(JsonNode weChatResult, Class<T> resultType) {

        checkCallWeChatSuccess(weChatResult);

        return JsonUtils.convertTo(weChatResult, resultType);
    }

    public static <T> T processWeChatResult(JsonNode weChatResult, String readNodePath, Class<T> resultType) {

        checkCallWeChatSuccess(weChatResult);

        return JsonUtils.readJsonValue(weChatResult, readNodePath, resultType);
    }

    public static <T> Map<String, T> processWeChatResult(JsonNode weChatResult, Map<String, Class<T>> readNodeMap) {

        checkCallWeChatSuccess(weChatResult);

        Map<String, T> resultMap = new HashMap<>();

        readNodeMap.forEach((k, v) -> {

            resultMap.put(k, JsonUtils.readJsonValue(weChatResult, k, v));
        });

        return resultMap;
    }

    private static void checkCallWeChatSuccess(JsonNode weChatResult) {

        Integer errCode = JsonUtils.readJsonValue(weChatResult, "errcode", Integer.class);

        if (errCode != null) {

            throw new RuntimeException(JsonUtils.readJsonValue(weChatResult, "errmsg", String.class));
        }
    }
}
