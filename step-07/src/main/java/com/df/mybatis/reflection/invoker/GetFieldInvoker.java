package com.df.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @Author df
 * @Description: getter 调用者
 * @Date 2024/3/12 17:28
 */
public class GetFieldInvoker implements Invoker{

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }



    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
