package df.middleware.mybatis.reflection.invoker;

/**
 * @description 调用者接口
 * @date 2022/5/2
 * 此接口的作用统一基于反射处理方法/属性的调用方式，是适配器（？忘了）模式的一种体现
 * 适配器模式(包装模式)：将一个类的接口适配成用户所期待的，原本由于接口不兼容而不能
 * 一起工作的类可以一起工作。
 * 调用策略
 */
public interface Invoker {

    /**
     * 执行反射操作
     *
     * @param target 方法或者属性执行的目标对象
     * @param args   方法或者属性执行时依赖的参数
     */
    Object invoke(Object target, Object[] args) throws Exception;

    /**
     *方法或者属性对应的类型
     */
    Class<?> getType();

}
