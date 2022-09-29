package com.spicy.wechat.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.spicy.util.JsonUtils;
import com.spicy.wechat.WeChatResult;
import com.spicy.wechat.contant.WeChatConstants;
import com.spicy.wechat.contant.WeChatUrl;
import com.spicy.wechat.model.request.Ticket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QrCodeResource {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessTokenResource accessTokenResource;

    public String getQrCodeUrl(String qrCodeType, Integer expireSeconds, String sceneStr, Integer sceneId) {

        return String.format(WeChatUrl.QR_CODE_URL, getTicket(qrCodeType, expireSeconds, sceneStr, sceneId));
    }

    public String getTicket(String qrCodeType, Integer expireSeconds, String sceneStr, Integer sceneId) {

        String requestTicketUrl = String.format(WeChatUrl.TICKET_URL, accessTokenResource.getAccessTokenValue());

        Ticket requestTicket = getRequestTicket(qrCodeType, expireSeconds, sceneStr, sceneId);

        String requestTicketStr = JsonUtils.toJson(requestTicket).toString();

        HttpEntity<?> httpEntity = new HttpEntity<>(requestTicketStr);

        ResponseEntity<JsonNode> requestResponse = restTemplate.exchange(requestTicketUrl, HttpMethod.POST, httpEntity, JsonNode.class);

        return WeChatResult.processWeChatResult(requestResponse.getBody(), "/ticket", String.class);
    }

    private Ticket getRequestTicket(String qrCodeType, Integer expireSeconds, String sceneStr, Integer sceneId) {

        Ticket ticket = new Ticket();

        ticket.setAction_name(qrCodeType);
        ticket.setExpire_seconds(expireSeconds);

        Ticket.Scene scene = new Ticket.Scene();
        Ticket.ActionInfo actionInfo = new Ticket.ActionInfo();

        if (StringUtils.equalsAny(qrCodeType, WeChatConstants.QrCodeType.QR_LIMIT_STR_SCENE, WeChatConstants.QrCodeType.QR_STR_SCENE)) {

            scene.setScene_str(sceneStr);
        } else {

            scene.setScene_id(sceneId);
        }

        actionInfo.setScene(scene);
        ticket.setAction_info(actionInfo);

        return ticket;
    }
}
