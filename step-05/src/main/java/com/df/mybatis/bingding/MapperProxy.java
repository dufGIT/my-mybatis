package com.df.mybatis.bingding;

import com.df.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 小傅哥，微信：fustack
 * @description 映射器代理类
 * @date 2022/3/26
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;

    private SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method,MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface,Map<Method,MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache=methodCache;
    }

    // 被代理器执行处理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            // 创建MapperMethod或从缓存中取出MapperMethod
            final MapperMethod mapperMethod=cachedMapperMethod(method);
            // 执行Sql语句
            return mapperMethod.execute(sqlSession,args);
        }
    }

    /**
     * 去缓存中找MapperMethod，找不到创建，创建完毕存储缓存
     */
    private MapperMethod cachedMapperMethod(Method method){
        MapperMethod mapperMethod=methodCache.get(method);
        if (mapperMethod==null){
            // 找不到才去new
            mapperMethod = new MapperMethod(mapperInterface,method,sqlSession.getConfiguration());
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }



}
