package com.spicy.system.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.spicy.mybatis.service.SpicyBaseService;
import com.spicy.system.entity.SysUser;

public interface ISysUserService extends SpicyBaseService<SysUser, String> {

    SysUser queryByOpenId(String openId);

    SysUser createSysUser(String openId, String sourceType);

    Boolean updateUserSubscribeStatusByOpenId(String openId, String subscribeStatus);

    Boolean updateSingleUserInfoByConditionField(SFunction<SysUser, ?> conditionField, Object conditionValue,
                                                 SFunction<SysUser, ?> updateField, Object updateValue);
}
