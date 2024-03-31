package com.df.mybatis.reflection;

import com.df.mybatis.reflection.invoker.GetFieldInvoker;
import com.df.mybatis.reflection.invoker.Invoker;
import com.df.mybatis.reflection.invoker.MethodInvoker;
import com.df.mybatis.reflection.invoker.SetFieldInvoker;
import com.df.mybatis.reflection.property.PropertyNamer;

import java.lang.reflect.*;
import java.lang.reflect.ReflectPermission;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author df
 * @Description: 反射器，属性 get/set 的映射器
 * @Date 2024/3/12 17:34
 */
public class Reflector {
    private static boolean classCacheEnabled = true;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    // 线程安全的缓存，缓存已经解析的类对象
    private static final Map<Class<?>, Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();

    private Class<?> type;
    // get 属性列表
    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;
    // set 属性列表
    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;
    // set 方法列表
    private Map<String, Invoker> setMethods = new HashMap<>();
    // get 方法列表
    private Map<String, Invoker> getMethods = new HashMap<>();
    // set 类型列表
    private Map<String, Class<?>> setTypes = new HashMap<>();
    // get 类型列表
    private Map<String, Class<?>> getTypes = new HashMap<>();
    // 构造函数
    private Constructor<?> defaultConstructor;

    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    public Reflector(Class<?> clazz) {
        this.type = clazz;
        // 加入构造函数
        addDefaultConstructor(clazz);
        // 加入 getter
        addGetMethods(clazz);
        // 加入 setter
        addSetMethods(clazz);
        // 加入字段
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
        // 获取getMethods的key,并将key转换大写
        for (String propName : readablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        // 获取setMethods的key,并将key转换大写
        for (String propName : writeablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
    }

    /**
     * 主要是用来获取某个类的无参构造函数，并将其设置为可访问
     */
    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : consts) {
            // 是否是午餐构造函数
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    // 这允许你即使构造函数是私有的，也可以实例化它。
                    constructor.setAccessible(true);
                }
            }
            // 检查构造函数是否已经是可访问的
            if (constructor.isAccessible()) {
                this.defaultConstructor = constructor;
            }
        }
    }

    /**
     * 获取get方法
     */
    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        // 获取当前类的方法
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                // 方法没有参数
                if (method.getParameterTypes().length == 0) {
                    // 获取方法名称前缀
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            }
        }
        // 将get方法放入get的全局变量中
        resolveGetterConflicts(conflictingGetters);
    }

    /**
     * 寻找set方法
     */
    private void addSetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3) {
                if (method.getParameterTypes().length == 1) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingSetters, name, method);
                }
            }
        }
        // 将set方法放入set的全局变量中
        resolveSetterConflicts(conflictingSetters);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Method firstMethod = setters.get(0);
            if (setters.size() == 1) {
                addSetMethod(propName, firstMethod);
            } else {
                Class<?> expectedType = getTypes.get(propName);
                if (expectedType == null) {
                    throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                            + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                            "specification and can cause unpredicatble results.");
                } else {
                    Iterator<Method> methods = setters.iterator();
                    Method setter = null;
                    while (methods.hasNext()) {
                        Method method = methods.next();
                        if (method.getParameterTypes().length == 1
                                && expectedType.equals(method.getParameterTypes()[0])) {
                            setter = method;
                            break;
                        }
                    }
                    if (setter == null) {
                        throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                                "specification and can cause unpredicatble results.");
                    }
                    addSetMethod(propName, setter);
                }
            }
        }
    }

    /**
     * 存储setter全局方法。
     */
    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(method));
            setTypes.put(name, method.getParameterTypes()[0]);
        }
    }

    /**
     *
     */
    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Ignored. This is only a final precaution, nothing we can do.
                }
            }
            if (field.isAccessible()) {
                if (!setMethods.containsKey(field.getName())) {
                    // 获取当前field的修饰符
                    int modifiers = field.getModifiers();
                    // 这个条件检查field是否同时是final和static,不是则执行括号的内容
                    if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                        addSetField(field);
                    }
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }


    /**
     * 设置字段代理方法放入全局变量中
     */
    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new SetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }

    /**
     * 设置获取字段代理方法放入全局变量中
     */
    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }

    /**
     * 最终通过的方法放入全局变量中
     * */
    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (String propName : conflictingGetters.keySet()) {
            List<Method> getters = conflictingGetters.get(propName);
            Iterator<Method> iterator = getters.iterator();
            Method firstMethod = iterator.next();
            // 某个属性只有一个对应的 getter 方法，那么直接调用 addGetMethod 方法将其添加到某个地方
            if (getters.size() == 1) {
                addGetMethod(propName, firstMethod);
            } else {
                // 代码首先获取第一个 getter 方法及其返回类型
                Method getter = firstMethod;
                Class<?> getterType = firstMethod.getReturnType();
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    Class<?> methodType = method.getReturnType();
                    // 如果某个 getter 方法的返回类型与第一个 getter 方法的返回类型相同，则抛出一个运行时异常，因为根据 JavaBean 规范，这是不允许的。
                    if (methodType.equals(getterType)) {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    } else if (methodType.isAssignableFrom(getterType)) {
                        // 如果某个 getter 方法的返回类型是第一个 getter 方法返回类型的子类（即返回类型更具体），那么这被认为是合法的，因为子类可以赋值给父类引用。
                        // OK getter type is descendant
                    } else if (getterType.isAssignableFrom(methodType)) {
                        // 如果第一个 getter 方法的返回类型是某个 getter 方法返回类型的子类（即第一个 getter 方法的返回类型更具体），则更新 getter 和 getterType 为当前这个 getter 方法和其返回类型。
                        getter = method;
                        getterType = methodType;
                    } else {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass()
                                + ".  This breaks the JavaBeans " + "specification and can cause unpredicatble results.");
                    }
                }
                addGetMethod(propName, getter);
            }
        }
    }

    /**
     * 添加方法到全局缓存
     */
    private void addGetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            getMethods.put(name, new MethodInvoker(method));
            getTypes.put(name, method.getReturnType());
        }
    }

    /**
     * 判断方法名称是不是有效的
     */
    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }


    /**
     * 将当前类放入缓存中，放入之前根据名称进行判断，如果之前没存过新建集合。
     */
    private void addMethodConflict(Map<String, List<Method>> conflictingGetters, String name, Method method) {
        List<Method> list = conflictingGetters.computeIfAbsent(name, k -> new ArrayList<>());
        list.add(method);
    }

    /**
     * 获取类里所有的方法
     */
    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<String, Method>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            // 将类里缓存放入uniqueMethods
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            // we also need to look for interface methods -
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            // 获取当前的父类
            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();
        return methods.toArray(new Method[methods.size()]);
    }


    /**
     * 将类里的方法放入缓存中
     * 1.不能重复，判断缓存中是否有当前方法了，判断依据为签名
     * 2.不存储桥接方法。
     * 3.如果都满足则把当前方法放入uniqueMethods缓存里
     */
    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            // 桥接方法和泛型有关系，桥接方法是Java为了支持泛型而引入的一种特殊方法，通常用于桥接类型之间的差异，保证1.5情况兼容的
            // 桥接是在编译器时自动加的方法，为了处理泛型
            if (!currentMethod.isBridge()) {
                //取得签名
                String signature = getSignature(currentMethod);

                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            // Ignored. This is only a final precaution, nothing we can do.
                        }
                    }
                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    /**
     * 获取签名
     * 签名组成：返回类型名称#方法名称:参数名称,参数名称 ==  void#stDrivere:java.lang.String
     */
    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        // 方法的返回类型
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            // 返回类型名称
            sb.append(returnType.getName()).append('#');
        }
        // 方法名称
        sb.append(method.getName());
        // 方法参数类型
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(":");
            } else {
                sb.append(",");
            }
            // 拼接参数类型
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }


    /**
     * 检查Java应用程序是否有权限访问私有方法。如果有权限，它返回true；否则，返回false
     * 在大多数未明确设置安全管理器的标准Java环境中，该方法通常返回true
     */
    private static boolean canAccessPrivateMethods() {
        try {
            // 返回当前应用程序的安全管理器，如果没有设置安全管理器，则返回null`
            SecurityManager securityManager = System.getSecurityManager();
            // 如果安全管理器存在
            if (null != securityManager) {
                // 尝试获取一个特定的权限,suppressAccessChecks 是一个特殊的权限，允许代码绕过Java的访问控制检查。这通常用于允许代码访问私有成员。
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            // 如果在尝试获取权限时抛出 SecurityException，则捕获该异常并返回 false。这表示当前的安全管理器不允许访问私有方法。
            return false;
        }
        return true;
    }

    public Constructor<?> getDefaultConstructor() {
        if (defaultConstructor != null) {
            return defaultConstructor;
        } else {
            throw new RuntimeException("There is no default constructor for " + type);
        }
    }

    public boolean hasDefaultConstructor() {
        return defaultConstructor != null;
    }

    public Class<?> getSetterType(String propertyName) {
        Class<?> clazz = setTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }

    public Invoker getGetInvoker(String propertyName) {
        Invoker method = getMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    public Invoker getSetInvoker(String propertyName) {
        Invoker method = setMethods.get(propertyName);
        if (method == null) {
            throw new RuntimeException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    /*
     * Gets the type for a property getter
     *
     * @param propertyName - the name of the property
     * @return The Class of the propery getter
     */
    public Class<?> getGetterType(String propertyName) {
        Class<?> clazz = getTypes.get(propertyName);
        if (clazz == null) {
            throw new RuntimeException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }

    /*
     * Gets an array of the readable properties for an object
     *
     * @return The array
     */
    public String[] getGetablePropertyNames() {
        return readablePropertyNames;
    }

    /*
     * Gets an array of the writeable properties for an object
     *
     * @return The array
     */
    public String[] getSetablePropertyNames() {
        return writeablePropertyNames;
    }

    /*
     * Check to see if a class has a writeable property by name
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a writeable property by the name
     */
    public boolean hasSetter(String propertyName) {
        return setMethods.keySet().contains(propertyName);
    }

    /*
     * Check to see if a class has a readable property by name
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a readable property by the name
     */
    public boolean hasGetter(String propertyName) {
        return getMethods.keySet().contains(propertyName);
    }

    public String findPropertyName(String name) {
        return caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    /*
     * Gets an instance of ClassInfo for the specified class.
     * 得到某个类的反射器，是静态方法，而且要缓存，又要多线程，所以REFLECTOR_MAP是一个ConcurrentHashMap
     *
     * @param clazz The class for which to lookup the method cache.
     * @return The method cache for the class
     */
    public static Reflector forClass(Class<?> clazz) {
        if (classCacheEnabled) {
            // synchronized (clazz) removed see issue #461
            // 对于每个类来说，我们假设它是不会变的，这样可以考虑将这个类的信息(构造函数，getter,setter,字段)加入缓存，以提高速度
            Reflector cached = REFLECTOR_MAP.get(clazz);
            if (cached == null) {
                // 反射器类里解析处理
                cached = new Reflector(clazz);
                REFLECTOR_MAP.put(clazz, cached);
            }
            return cached;
        } else {
            return new Reflector(clazz);
        }
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        Reflector.classCacheEnabled = classCacheEnabled;
    }

    public static boolean isClassCacheEnabled() {
        return classCacheEnabled;
    }


}
