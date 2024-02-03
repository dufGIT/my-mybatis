package com.df.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Author df
 *
 * @Description:
 * @Date 2024/1/23 20:57
 */
public class MapperProxyFactory <T>{
    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(Map<String, String> sqlSession) {
        final MapperProxy<T> mapperProxy=new MapperProxy<>(sqlSession,mapperInterface);
        // 第1个参数：类加载器，第2个参数：被代理的类，第3个参数：实际执行类InvocationHandler
        return (T)  Proxy.newProxyInstance(mapperInterface.getClassLoader(),new Class[]{mapperInterface},mapperProxy);
    }

}
