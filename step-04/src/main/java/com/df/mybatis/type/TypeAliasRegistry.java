package com.df.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Author df
 * @Description: 类型别名注册机
 * @Date 2024/2/16 15:38
 */
public class TypeAliasRegistry {
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

    public TypeAliasRegistry() {
        // 构造函数里注册系统内置的类型别名
        registerAlias("string", String.class);

        // 基本包装类型
        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);
    }

    // 别名注册
    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIASES.put(key, value);
    }

    // 取出别名的类
    public <T> Class<T> resolveAlias(String string) {
        String key = string.toLowerCase(Locale.ENGLISH);
        return (Class<T>) TYPE_ALIASES.get(key);
    }


}
