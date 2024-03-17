package com.df.mybatis.reflection.wrapper;

import com.df.mybatis.reflection.MetaObject;

/**
 * @Author df
 * @Description: 默认对象包装工厂
 * @Date 2024/3/16 15:12
 */
public class DefaultObjectWrapperFactory implements  ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
