package com.spicy.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.spicy.data.DataEntity;
import com.spicy.rule.Operator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SpicyQueryWrapper<T extends DataEntity<ID>, ID extends Serializable, R> extends LambdaQueryWrapper<T> {

    public LambdaQueryWrapper<T> initBaseQueryWrapper(SFunction<T, R> conditionField, R conditionValue, Operator operator) {

        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();

        return null;
    }

    public static void main(String[] args) {


    }
}
