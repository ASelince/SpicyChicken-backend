package com.spicy.wechat.controller;

import com.spicy.util.JsonUtils;
import com.spicy.wechat.config.WeChatConfig;
import com.spicy.wechat.resource.ScanCodeResource;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/weChat")
public class ScanCodeController {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private ScanCodeResource scanCodeResource;


    @RequestMapping(value = "/scanCode/callBack", method = RequestMethod.GET)
    public String tokenVerify(@RequestParam(value = "signature", required = false) String signature,
                              @RequestParam(value = "timestamp", required = false) String timestamp,
                              @RequestParam(value = "nonce", required = false) String nonce,
                              @RequestParam(value = "echostr", required = false) String echostr) {


        String verifyStr = SHA1.gen(weChatConfig.getToken(), timestamp, nonce);

        if (StringUtils.equals(signature, verifyStr)) {

            return echostr;
        }

        throw new RuntimeException("Unknown source! Please don't visit me casually!");
    }

    @RequestMapping(value = "/scanCode/callBack", method = RequestMethod.POST)
    public String scanCallBack(@RequestBody String body) {

        WxMpXmlMessage weChatMessage = WxMpXmlMessage.fromXml(body);

        scanCodeResource.doSomething(weChatMessage);

        System.out.println(JsonUtils.toJson(weChatMessage));
        return "success";
    }
}
