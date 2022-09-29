package com.spicy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.spicy.mybatis.service.SpicyBaseServiceImpl;
import com.spicy.system.constants.SysConstants;
import com.spicy.system.entity.SysUser;
import com.spicy.system.mapper.SysUserMapper;
import com.spicy.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SysUserServiceImpl extends SpicyBaseServiceImpl<SysUserMapper, SysUser, String> implements ISysUserService {

    @Override
    public SysUser queryByOpenId(String openId) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getOpenId, openId);

        return getOne(queryWrapper);
    }

    @Override
    public SysUser createSysUser(String openId, String sourceType) {

        SysUser user = new SysUser();

        user.setOpenId(openId);
        user.setSourceType(sourceType);

        String name = SysConstants.UserPrefix.WE_CHAT_PREFIX + System.currentTimeMillis();
        user.setUsername(name);
        user.setNickName(name);

        user.setCreatedTime(new Date());
        user.setCreatedBy(SysConstants.SystemUserInfo.CREATED_USER_ID);
        user.setCreatedByName(SysConstants.SystemUserInfo.CREATED_USER_NAME);

        save(user);
        return user;
    }

    @Override
    public Boolean updateUserSubscribeStatusByOpenId(String openId, String subscribeStatus) {

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getOpenId, openId);
        updateWrapper.set(SysUser::getSubscribeStatus, subscribeStatus);

        return update(updateWrapper);
    }

    @Override
    public Boolean updateSingleUserInfoByConditionField(SFunction<SysUser, ?> conditionField, Object conditionValue,
                                                        SFunction<SysUser, ?> updateField, Object updateValue) {

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(conditionField, conditionValue);
        updateWrapper.set(updateField, updateValue);

        return update(updateWrapper);
    }

}
