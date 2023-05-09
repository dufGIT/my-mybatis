package df.middleware.mybatis.session;

import com.mysql.fabric.xmlrpc.base.Param;
import df.middleware.mybatis.binding.MapperRegistry;
import df.middleware.mybatis.datasource.druid.DruidDataSourceFactory;
import df.middleware.mybatis.datasource.pooled.PooledDataSourceFactory;
import df.middleware.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.executor.SimpleExecutor;
import df.middleware.mybatis.executor.parameter.ParameterHandler;
import df.middleware.mybatis.executor.resultset.DefaultResultSetHandler;
import df.middleware.mybatis.executor.resultset.ResultSetHandler;
import df.middleware.mybatis.executor.statement.PreparedStatementHandler;
import df.middleware.mybatis.executor.statement.StatementHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.Environment;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.reflection.factory.DefaultObjectFactory;
import df.middleware.mybatis.reflection.factory.ObjectFactory;
import df.middleware.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import df.middleware.mybatis.reflection.wrapper.ObjectWrapperFactory;
import df.middleware.mybatis.scripting.LanguageDriver;
import df.middleware.mybatis.scripting.LanguageDriverRegistry;
import df.middleware.mybatis.scripting.xmltags.XMLLanguageDriver;
import df.middleware.mybatis.transaction.Transaction;
import df.middleware.mybatis.transaction.jdbc.JdbcTransactionFactory;
import df.middleware.mybatis.type.TypeAliasRegistry;
import df.middleware.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// todo 第四章加
public class Configuration {

    //环境
    protected Environment environment;

    // 映射注册机
    protected MapperRegistry mapperRegistry = new MapperRegistry();

    // 映射的语句，存在map里
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
    // 类型处理器注册机
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();


    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    protected String databaseId;

    protected final Set<String> loadedResources = new HashSet<>();

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);

        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        // 注册xml语言驱动器
        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
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
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter,rowBounds, resultHandler, boundSql);
    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, resultHandler, rowBounds, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

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

    public ParameterHandler newParameterHandler(MappedStatement statement, Object paramObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = statement.getLang().createParameterHandler(statement, paramObject, boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容 interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    // 类型处理器注册机
    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }
}
