package com.spicy.wechat.resource;

import com.spicy.constants.SharedConstants;
import com.spicy.system.entity.SysUser;
import com.spicy.system.service.ISysUserService;
import com.spicy.wechat.contant.WeChatConstants;
import com.spicy.wechat.model.response.WeChatScanResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScanCodeResource {

    @Autowired
    private ISysUserService userSvc;

    @Transactional
    public Object doSomething(WxMpXmlMessage weChatMessage) {


        WeChatScanResult scanResult = _toWeChatResult(weChatMessage);

        if (StringUtils.equals(scanResult.getEvent(), WeChatConstants.CALL_BACK_TYPE.SUBSCRIBE)) {

            doSubscribe(scanResult);
        }

        if (StringUtils.equals(scanResult.getEvent(), WeChatConstants.CALL_BACK_TYPE.SCAN)) {

            doScan(scanResult);
        }

        if (StringUtils.equals(scanResult.getEvent(), WeChatConstants.CALL_BACK_TYPE.UN_SUBSCRIBE)) {

            doUnSubscribe(scanResult);
        }

        return "success";
    }

    private void doSubscribe(WeChatScanResult scanResult) {

        SysUser sysUser = userSvc.queryByOpenId(scanResult.getOpenId());

        if (sysUser == null) {

            sysUser = userSvc.createSysUser(scanResult.getOpenId(), WeChatConstants.Sync.CREATE_USER);
        }

        if (StringUtils.equals(sysUser.getSubscribeStatus(), SharedConstants.BoolConstants.TRUE_STR_NUM)) {

            userSvc.updateUserSubscribeStatusByOpenId(sysUser.getOpenId(), SharedConstants.BoolConstants.FALSE_STR_NUM);
        }

        //todo login
    }

    private void doScan(WeChatScanResult scanResult) {

        //todo login
    }

    private void doUnSubscribe(WeChatScanResult scanResult) {

        userSvc.updateUserSubscribeStatusByOpenId(scanResult.getOpenId(), SharedConstants.BoolConstants.FALSE_STR_NUM);
    }

    private WeChatScanResult _toWeChatResult(WxMpXmlMessage weChatMessage) {

        WeChatScanResult result = new WeChatScanResult();

        result.setOpenId(weChatMessage.getFromUser());
        result.setEvent(weChatMessage.getEvent());
        result.setTicket(weChatMessage.getTicket());
        result.setMessageFrom(weChatMessage.getToUser());
        result.setEventKey(weChatMessage.getEventKey());

        return result;
    }
}
