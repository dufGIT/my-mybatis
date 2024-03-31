package com.df.mybatis.reflection.invoker;

/**
 * @Author df
 * @Description: 调用者
 * @Date 2024/3/12 17:20
 */
public interface Invoker {
    Object invoke(Object target, Object[] args) throws Exception;
    Class<?> getType();
}
