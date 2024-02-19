package com.df.mybatis.session;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.datasource.druid.DruidDataSourceFactory;
import com.df.mybatis.mapping.Environment;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.transation.jdbc.JdbcTransactionFactory;
import com.df.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author df
 * @Description: 配置项,串联整个流程的对象保存操作
 * @Date 2024/2/7 12:40
 */
public class Configuration {

    //环境
    protected Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);
    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();
    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    // 添加类型别名注册机，通过构造函数添加 JDBC、DRUID 注册操作。
    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
    }


    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }


    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


}
