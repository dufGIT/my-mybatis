package com.df.mybatis.binding;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author df
 * @Description: 映射器代理类
 * @Date 2024/1/23 20:26
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;

    private Map<String,String> sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface=mapperInterface;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        }else{
            return "你的被代理了！"+sqlSession.get(mapperInterface.getName()+"."+method.getName());
        }
    }
}
