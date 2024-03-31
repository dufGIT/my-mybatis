package com.df.mybatis.reflection.wrapper;

import com.df.mybatis.reflection.MetaObject;

/**
 * @Author df
 * @Description: 对象包装工厂
 * @Date 2024/3/16 15:11
 */
public interface ObjectWrapperFactory {
    /**
     * 判断有没有包装器
     */
    boolean hasWrapperFor(Object object);

    /**
     * 得到包装器
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
