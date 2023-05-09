package df.middleware.mybatis.type;

import df.middleware.mybatis.io.Resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// todo 第四章加
// 基础类型别名注册机
// 注意Registry意思是登记处  ,,  register：是登记
public class TypeAliasRegistry {
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    public TypeAliasRegistry() {
        // 构造函数里注册系统内置的类型别名
        registerAlias("string", String.class);
        // 基本包装类型
        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("double", Double.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("short", Short.class);
    }

    // 注册别名
    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIASES.put(key, value);
    }

    public <T> Class<T> resolveAlias(String string) {
        try {
            if (string == null) {
                return null;
            }
            String key = string.toLowerCase(Locale.ENGLISH);
            Class<T> value;
            if (TYPE_ALIASES.containsKey(key)) {
                value = (Class<T>) TYPE_ALIASES.get(key);
            } else {
                // 反射获取类数据
                value = (Class<T>) Resources.classForName(string);
            }
            return value;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
        }
    }

}
