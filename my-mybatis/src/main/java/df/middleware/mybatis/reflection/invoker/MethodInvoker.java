package df.middleware.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * @description 方法调用者
 * 用于执行方法的反射操作
 */
public class MethodInvoker implements Invoker {

    private Class<?> type;
    private Method method;
    /**
     * 如果方法是getter方法，则表示返回值类型
     * 如果方法是setter方法，则表示入参类型
     */
    public MethodInvoker(Method method) {
        this.method = method;

        // 利用方法是否有无入参判断方法是get还是set方法，
        // 如果只有一个参数，返回参数类型，否则返回 return 类型
        if (method.getParameterTypes().length == 1) {
            // set方法获取方法入参类型赋给type属性
            type = method.getParameterTypes()[0];
        } else {
            // get方法则获取方法返回类型赋给type属性
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        // 执行方法
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }

}
