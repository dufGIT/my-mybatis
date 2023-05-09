package df.middleware.mybatis.reflection;



import df.middleware.mybatis.reflection.factory.ObjectFactory;
import df.middleware.mybatis.reflection.property.PropertyTokenizer;
import df.middleware.mybatis.reflection.wrapper.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @description 元对象
 * */
public class MetaObject {
    // 原对象
    private Object originalObject;
    /**
     * 封装过的 Object 对象
     */
    // 对象包装器
    private ObjectWrapper objectWrapper;
    // 对象工厂
    private ObjectFactory objectFactory;
    // 对象包装工厂
    private ObjectWrapperFactory objectWrapperFactory;

    // 赋值为不同的包装器
    private MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;

        if (object instanceof ObjectWrapper) {
            // 如果对象本身已经是ObjectWrapper型，则直接赋给objectWrapper
            this.objectWrapper = (ObjectWrapper) object;
        } else if (objectWrapperFactory.hasWrapperFor(object)) {
            // 如果有包装器,调用ObjectWrapperFactory.getWrapperFor
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
        } else if (object instanceof Map) {
            // 如果是Map型，返回MapWrapper
            this.objectWrapper = new MapWrapper(this, (Map) object);
        } else if (object instanceof Collection) {
            // 如果是Collection型，返回CollectionWrapper
            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        } else {
            // 除此以外，返回BeanWrapper
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    /**
     * 创建 MetaObject 对象
     *
     * @param object 原始 Object 对象
     * @param objectFactory
     * @param objectWrapperFactory
     * @return MetaObject 对象
     */
    public static MetaObject forObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        if (object == null) {
            // 处理一下null,将null包装起来
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object, objectFactory, objectWrapperFactory);
        }
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    /* --------以下方法都是委派给 ObjectWrapper------ */
    // 查找属性，相当于又封装了一层
    public String findProperty(String propName, boolean useCamelCaseMapping) {
        return objectWrapper.findProperty(propName, useCamelCaseMapping);
    }

    /**
     * 获取get方法属性名称和get方法去掉get后边的属性名称
     * getIds(){return id;} --> (id、ids)
     * @return
     */
    // 取得getter的名字列表
    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }

    /**
     * 获取set方法属性名称和set方法去掉set后边的属性名称
     * setIds(){return id;} --> (id、ids)
     * @return
     */
    // 取得setter的名字列表
    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

    /**
     * 获取set方法后边属性的类型
     * @param name 这个name 要和setXXX方法中的XXX相同才能获取到，否则抛异常
     * @return
     */
    // 取得setter的类型列表
    public Class<?> getSetterType(String name) {
        return objectWrapper.getSetterType(name);
    }

    /**
     * 获取get方法后边属性的类型
     * @param name 这个name，要个getXXX方法中的XXX相同才能获取到，否则抛异常
     * @return
     */
    // 取得getter的类型列表
    public Class<?> getGetterType(String name) {
        return objectWrapper.getGetterType(name);
    }

    /**
     * 判断name是否是setXXX()方法中的XXX
     * @param name
     * @return
     */
    //是否有指定的setter
    public boolean hasSetter(String name) {
        return objectWrapper.hasSetter(name);
    }

    /**
     * 判断name是否是getXXX()方法中的XXX
     * @param name
     * @return
     */
    // 是否有指定的getter
    public boolean hasGetter(String name) {
        return objectWrapper.hasGetter(name);
    }

    /**
     * 获取对象属性值，可以递归获取
     * @param name
     * @return
     */
    // 取得值
    // 如 班级[0].学生.成绩
    public Object getValue(String name) {
        // 创建 PropertyTokenizer 对象，对 name 分词
        PropertyTokenizer prop = new PropertyTokenizer(name);
        // 有子表达式
        if (prop.hasNext()) {
            // 创建 MetaObject 对象,递归调用
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            // 递归判断子表达式 children ，获取值，metaValue == null，则返回null
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                // 如果上层就是null了，那就结束，返回null
                return null;
            } else {
                // 否则继续看下一层，递归调用getValue
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            // 无子表达式，取值
            return objectWrapper.get(prop);
        }
    }

    /**
     * 给对象属性设置值，可以递归设置，基本类型，数组，对象，都可以自动创建
     * 但是ArrayList和数组需要手动创建
     * List必须创建对象，添加进list
     * @param name
     * @param value
     */
    // 如 班级[0].学生.成绩
    public void setValue(String name, Object value) {
        // 创建 PropertyTokenizer 对象，对 name 分词
        PropertyTokenizer prop = new PropertyTokenizer(name);
        // 有子表达式
        if (prop.hasNext()) {
            // 创建 MetaObject 对象
            MetaObject metaValue = metaObjectForProperty(prop.getIndexedName());
            // 递归判断子表达式 children ，设置值
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null && prop.getChildren() != null) {
                    // don't instantiate child path if value is null
                    // 如果上层就是 null 了，还得看有没有儿子，没有那就结束
                    return;
                } else {
                    // 创建值
                    // 否则还得 new 一个，委派给 ObjectWrapper.instantiatePropertyValue
                    metaValue = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
                }
            }
            // 递归调用setValue
            metaValue.setValue(prop.getChildren(), value);
        } else {
            // 到了最后一层了，所以委派给 ObjectWrapper.set
            objectWrapper.set(prop, value);
        }
    }

    // 为属性生成元对象
    public MetaObject metaObjectForProperty(String name) {
        // 实际是递归调用
        Object value = getValue(name);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }

    public ObjectWrapper getObjectWrapper() {
        return objectWrapper;
    }

    // 是否是集合
    public boolean isCollection() {
        return objectWrapper.isCollection();
    }

    // 添加属性
    public void add(Object element) {
        objectWrapper.add(element);
    }

    // 添加属性
    public <E> void addAll(List<E> list) {
        objectWrapper.addAll(list);
    }

}
