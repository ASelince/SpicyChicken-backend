package com.spicy.wechat.contant;

public class WeChatUrl {

    /**
     * Get AccessToken URL
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    /**
     * Get Ticket
     */
    public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    /**
     * Get QrCode
     */
    public static final String QR_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
}
