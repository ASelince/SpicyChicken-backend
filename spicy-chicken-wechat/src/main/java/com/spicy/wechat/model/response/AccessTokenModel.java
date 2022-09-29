package com.spicy.wechat.model.response;

import lombok.Data;

@Data
public class AccessTokenModel {

    private Long expires_in;
    private String access_token;
}
