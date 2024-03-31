package com.df.mybatis.session;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.datasource.druid.DruidDataSourceFactory;
import com.df.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.df.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.df.mybatis.executor.Executor;
import com.df.mybatis.executor.SimpleExecutor;
import com.df.mybatis.executor.resultset.DefaultResultSetHandler;
import com.df.mybatis.executor.resultset.ResultSetHandler;
import com.df.mybatis.executor.statement.PreparedStatementHandler;
import com.df.mybatis.executor.statement.StatementHandler;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.Environment;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.reflection.MetaObject;
import com.df.mybatis.reflection.factory.DefaultObjectFactory;
import com.df.mybatis.reflection.factory.ObjectFactory;
import com.df.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.df.mybatis.reflection.wrapper.ObjectWrapperFactory;
import com.df.mybatis.scripting.LanguageDriverRegistry;
import com.df.mybatis.scripting.xmltags.XMLLanguageDriver;
import com.df.mybatis.transation.Transaction;
import com.df.mybatis.transation.jdbc.JdbcTransactionFactory;
import com.df.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author df
 * @Description: 配置项, 串联整个流程的对象保存操作
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
    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();
    // mapper资源存储
    protected final Set<String> loadedResources = new HashSet<>();

    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();


    // 添加类型别名注册机，通过构造函数添加 JDBC、DRUID 注册操作。
    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        // 设置XMLLanguageDriver为默认语言驱动并注册到里。
        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
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

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }

    /**
     * 判断当前mapper资源是否被加载
     */
    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    /**
     * 添加当前mapper资源存储到集合里
     */
    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    // 创建元对象
    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }





}
