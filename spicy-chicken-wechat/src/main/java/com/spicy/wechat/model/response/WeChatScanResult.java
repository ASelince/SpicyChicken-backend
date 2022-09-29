package com.spicy.wechat.model.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeChatScanResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId;

    private String event;

    private String eventKey;

    private String messageFrom;

    private String ticket;
}
