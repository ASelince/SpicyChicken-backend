package com.spicy.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spicy.data.DataEntity;

import java.io.Serializable;

public interface SpicyBaseService<T extends DataEntity<ID>, ID extends Serializable> extends IService<T> {

    /**
     * Query By Primary Key
     */
    T queryById(ID value);

    /**
     * Query By Primary Key
     */
    T queryById(ID value, boolean isThrowException);

    /**
     * Query By Primary Key
     */
    T queryById(ID value, boolean isThrowException, String exceptionMessage);
}
