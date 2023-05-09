package df.middleware.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * @description getter 调用者
 * 属性是会有set和get的，所以字段会有GetFieldInvoker和SetFieldInvoker
 * 此类用于执行读取属性值的反射操作
 */
public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        // 基于反射获取指定属性值
        return field.get(target);
    }

    // 获取对应属性的类型
    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
