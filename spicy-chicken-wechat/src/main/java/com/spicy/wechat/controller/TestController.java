package com.spicy.wechat.controller;

import com.spicy.wechat.contant.WeChatConstants;
import com.spicy.wechat.resource.QrCodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private QrCodeResource qrCodeResource;

    @RequestMapping(value = "/getQrCode", method = RequestMethod.GET)
    public String tt() {

        return qrCodeResource.getQrCodeUrl(WeChatConstants.QrCodeType.QR_STR_SCENE, 1000, "CA", null);
    }
}
