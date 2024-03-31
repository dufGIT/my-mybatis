package com.df.mybatis.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author df
 * @Description: 脚本语言注册器
 * @Date 2024/3/18 15:48
 */
public class LanguageDriverRegistry {
    // map
    private final Map<Class<?>, LanguageDriver> LANGUAGE_DRIVER_MAP = new HashMap<Class<?>, LanguageDriver>();
    private Class<?> defaultDriverClass = null;

    public void register(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null is not a valid Language Driver");
        }
        if (!LanguageDriver.class.isAssignableFrom(cls)) {
            throw new RuntimeException(cls.getName() + " does not implements " + LanguageDriver.class.getName());
        }
        LanguageDriver driver = LANGUAGE_DRIVER_MAP.get(cls);
        if (driver == null) {
            try {
                // 实例化驱动
                driver = (LanguageDriver) cls.newInstance();
                // 将语言驱动器放入缓存中
                LANGUAGE_DRIVER_MAP.put(cls, driver);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to load language driver for " + cls.getName(), ex);
            }
        }
    }

    public LanguageDriver getDriver(Class<?> cls) {
        return LANGUAGE_DRIVER_MAP.get(cls);
    }

    public LanguageDriver getDefaultDriver() {
        return getDriver(getDefaultDriverClass());
    }

    // 获取默认语言驱动器
    public Class<?> getDefaultDriverClass() {
        return defaultDriverClass;
    }

    //Configuration()有调用，默认的为XMLLanguageDriver
    public void setDefaultDriverClass(Class<?> defaultDriverClass) {
        register(defaultDriverClass);
        this.defaultDriverClass = defaultDriverClass;
    }


}
