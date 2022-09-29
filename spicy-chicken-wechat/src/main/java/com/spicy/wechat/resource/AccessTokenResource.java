package com.spicy.wechat.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.spicy.wechat.WeChatResult;
import com.spicy.wechat.config.WeChatConfig;
import com.spicy.wechat.contant.GrantTypeEnum;
import com.spicy.wechat.contant.WeChatUrl;
import com.spicy.wechat.model.response.AccessTokenModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AccessTokenResource {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private RestTemplate restTemplate;


    public String getAccessTokenValue() {

        return getAccessToken().getAccess_token();
    }

    private AccessTokenModel getAccessToken() {

        String accessTokenUrl = String.format(WeChatUrl.ACCESS_TOKEN_URL, GrantTypeEnum.CLIENT_CREDENTIAL, weChatConfig.getAppId(), weChatConfig.getAppSecret());

        JsonNode accessTokenResult = restTemplate.getForObject(accessTokenUrl, JsonNode.class);

        AccessTokenModel result = WeChatResult.processWeChatResult(accessTokenResult, AccessTokenModel.class);

        //todo setRedis or Refresh redis
        return result;
    }
}
