package df.middleware.mybatis.reflection.wrapper;

import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.reflection.factory.ObjectFactory;
import df.middleware.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * @description 对象包装器
 * 是对对象的包装的接口，抽象了对象的字段信息、 getter| setter 方法、和上面三个成员的数据类型,
 * 它定义了一系列查询对象属性信息的方法,以及更新属性的方法 。添加属性方法
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
