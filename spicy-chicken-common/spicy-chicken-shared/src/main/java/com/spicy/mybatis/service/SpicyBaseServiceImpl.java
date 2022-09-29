package com.spicy.mybatis.service;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spicy.data.DataEntity;

import java.io.Serializable;

public abstract class SpicyBaseServiceImpl<M extends BaseMapper<T>, T extends DataEntity<ID>, ID extends Serializable> extends ServiceImpl<M, T> implements SpicyBaseService<T, ID> {

    @Override
    public T queryById(ID value) {

        return getById(value);
    }

    @Override
    public T queryById(ID value, boolean isThrowException) {

        return queryById(value, isThrowException, null);
    }

    @Override
    public T queryById(ID value, boolean isThrowException, String exceptionMessage) {

        T t = queryById(value);

        if (t == null && isThrowException) {

            if (exceptionMessage == null) {

                throw new RuntimeException(String.format("Not found this Primary Key [%s] corresponding data!", value));
            } else {

                throw new RuntimeException(exceptionMessage);
            }
        }

        return t;
    }
}
