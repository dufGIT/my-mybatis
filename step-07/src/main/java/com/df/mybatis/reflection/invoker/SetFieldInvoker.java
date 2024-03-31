package com.df.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @Author df
 * @Description: setter 调用者
 * @Date 2024/3/12 17:30
 */
public class SetFieldInvoker implements Invoker {
    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

}
