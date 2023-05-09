package df.middleware.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @description setter 调用者
 * 用于执行设置属性值的反射操作
 * @date 2022/5/2
 */
public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        // 为指定属性设置值的功能
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}