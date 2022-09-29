package com.spicy.system.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spicy.data.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName(value = "sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends DataEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String openId;

    private String nickName;

    private String username;

    private String userEmail;

    private String userPhone;

    private String userPassword;

    private String sourceType;

    private String subscribeStatus;

    private Date latelyLoginTime;
}
