package com.spicy.wechat.contant;

/**
 * Access WeChat Enum
 */
public enum GrantTypeEnum {


    //GET AccessToken
    CLIENT_CREDENTIAL("client_credential");

    private final String key;

    private GrantTypeEnum(String key) {

        this.key = key;
    }

    public String toString() {

        return this.key;
    }
}
