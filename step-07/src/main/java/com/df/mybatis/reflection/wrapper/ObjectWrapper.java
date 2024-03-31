package com.df.mybatis.reflection.wrapper;

import com.df.mybatis.reflection.MetaObject;
import com.df.mybatis.reflection.factory.ObjectFactory;
import com.df.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * @Author df
 * @Description: 对象包装器
 * @Date 2024/3/15 14:59
 */
public interface ObjectWrapper {
    // get
    Object get(PropertyTokenizer prop);
    // set
    void set(PropertyTokenizer prop, Object value);
    // 查找属性
    String findProperty(String name, boolean useCamelCaseMapping);
    // 取得getter的名字列表
    String[] getGetterNames();
    // 取得setter的名字列表
    String[] getSetterNames();
    //取得setter的类型
    Class<?> getSetterType(String name);
    // 取得getter的类型
    Class<?> getGetterType(String name);
    // 是否有指定的setter
    boolean hasSetter(String name);
    // 是否有指定的getter
    boolean hasGetter(String name);
    // 实例化属性
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);
    // 是否是集合
    boolean isCollection();
    // 添加属性
    void add(Object element);
    // 添加属性
    <E> void addAll(List<E> element);


}
