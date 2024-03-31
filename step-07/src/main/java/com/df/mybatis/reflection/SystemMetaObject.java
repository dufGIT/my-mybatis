package com.df.mybatis.reflection;

import com.df.mybatis.reflection.factory.DefaultObjectFactory;
import com.df.mybatis.reflection.factory.ObjectFactory;
import com.df.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.df.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * @Author df
 * @Description: 一些系统级别的元对象
 * @Date 2024/3/12 17:18
 */
public class SystemMetaObject {
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
}
