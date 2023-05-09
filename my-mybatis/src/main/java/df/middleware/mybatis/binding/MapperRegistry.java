package df.middleware.mybatis.binding;

import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.SqlSession;
import cn.hutool.core.lang.ClassScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// todo 第三章加
// 映射器注射机
// 处理代理类的添加和获取以及调用完成代理
/***
 * 映射注册器，
 * addMapper :往容器里放入映射工厂
 * getMapper ：通过代理工厂创建代理对象
 *
 *
 *   1.开始从解析xml的namespace调用configuration.addMapper
 *   2.然后调用namespace代理的具体方法getMapper()，开始执行后续代理操作
 *   mapperRegistry
 *      通过getMapper进入代理工厂
 *   MapperProxyFactory
 *       获取代理对象，当代理对象获取对应方法时，执行MapperProxy
 *   MapperProxy
 *       执行invoke代理方法，执行MapperMethod
 *   MapperMethod
 *       执行execute()进行sqlSession.selectone()，一整个代理nameSpace过程
 *
 */
public class MapperRegistry {


    // 将已添加的映射代理加入到HashMap
    // 将上期的 MapperProxy改为MapperProxyFactory去工厂调用将namespace进行代理
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    // 在容器中拿到原类的代理工厂
    // 通过代理工厂创建代理对象并返回代理对象
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }

        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause" + e, e);
        }
    }

    // 注册映射器-处理的是xml的namespace写的string实体，所以需要反射namespace得到类以后调用此方法
    // 放入注册映射器
    public <T> void addMapper(Class<T> type) {
        // 是接口
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            knownMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }


}
