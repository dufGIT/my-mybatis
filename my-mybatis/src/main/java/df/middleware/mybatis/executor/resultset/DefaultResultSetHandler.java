package df.middleware.mybatis.executor.resultset;

import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.executor.result.DefaultResultContext;
import df.middleware.mybatis.executor.result.DefaultResultHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.ResultMap;
import df.middleware.mybatis.mapping.ResultMapping;
import df.middleware.mybatis.reflection.MetaClass;
import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.reflection.factory.ObjectFactory;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;
import df.middleware.mybatis.type.TypeHandler;
import df.middleware.mybatis.type.TypeHandlerRegistry;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Author df
 * @Date 2022/6/13 14:28
 * @Version 1.0
 * @description 默认Map结果处理器
 * 对jdbc结果集进行封装
 */
public class DefaultResultSetHandler implements ResultSetHandler {
    private final BoundSql boundSql;
    private final MappedStatement mappedStatement;
    private final Configuration configuration;
    private final RowBounds rowBounds;
    private final ResultHandler resultHandler;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final ObjectFactory objectFactory;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ResultHandler resultHandler, RowBounds rowBounds, BoundSql boundSql) {
        this.boundSql = boundSql;
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.rowBounds = rowBounds;
        this.resultHandler = resultHandler;
        this.objectFactory = configuration.getObjectFactory();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }

    /**
     * 返回结果集的处理流程
     * 1.调用结果集包装器处理JDBC返回的数据
     * 2.获取resultMap也就知道了返回对象，将返回对象类传入下一流程中
     * 3.handleResultSet()这个方法后就是一整个流程，实例化对象、找到未被映射的属性，将这些属性反射到对象中
     * 4.将处理过的对象存储到结果处理器，结果上下文中，并返回结果处理器中数据
     * */
    @Override
    public List<Object> handleResultSets(Statement stmt) throws SQLException {
        final List<Object> multipleResults = new ArrayList<>();

        int resultSetCount = 0;
        // 处理返回的列名，类型等数据
        ResultSetWrapper rsw = new ResultSetWrapper(stmt.getResultSet(), configuration);
        // 从mappedStatement获取ResultMap（ResultMap里有id和类型以及ResultMapping）
        List<ResultMap> resultMaps = mappedStatement.getResultMaps();
        while (rsw != null && resultMaps.size() > resultSetCount) {
            ResultMap resultMap = resultMaps.get(resultSetCount);
            // 处理封装保存结果
            handleResultSet(rsw, resultMap, multipleResults, null);
            rsw = getNextResultSet(stmt);
            resultSetCount++;
        }
        return multipleResults.size() == 1 ? (List<Object>) multipleResults.get(0) : multipleResults;
    }

    private ResultSetWrapper getNextResultSet(Statement stmt) {
        try {
            if (stmt.getConnection().getMetaData().supportsMultipleResultSets()) {
                if (!((!stmt.getMoreResults()) && (stmt.getUpdateCount() == -1))) {
                    ResultSet rs = stmt.getResultSet();
                    return rs != null ? new ResultSetWrapper(rs, configuration) : null;
                }
            }
        } catch (Exception ihnore) {

        }
        return null;
    }

    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults, ResultMapping parentMapping) throws SQLException {
        if (resultHandler == null) {
            // 1.新创建结果处理器
            DefaultResultHandler defaultResultHandler = new DefaultResultHandler(objectFactory);
            // 2.封装结果数据
            handleRowValuesForSimpleResultMap(rsw, resultMap, defaultResultHandler, rowBounds, null);
            // 3.保存结果,从resultHandler拿到上下文结果数据
            multipleResults.add(defaultResultHandler.getResultList());
        }
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        // 实例化默认结果上下文
        DefaultResultContext resultContext = new DefaultResultContext();
        while (resultContext.getResultCount() < rowBounds.getLimit() && rsw.getResultSet().next()) {
            // 为对象赋值
            // 1.实例化对象
            // 2.找到未被映射的属性名称
            // 3.根据属性名称进行一系列反射工具反射设置对象属性值.
            Object rowValue = getRowValue(rsw, resultMap);
            // 存储结果
            callResultHandler(resultHandler, resultContext, rowValue);
        }
    }

    /**
     * 返回结果存储
     * */
    private void callResultHandler(ResultHandler resultHandler, DefaultResultContext resultContext, Object rowValue) throws SQLException {
        // 将返回结果赋值给resultContext
        resultContext.nextResultObject(rowValue);
        // 将结果存储到resultHandler
        resultHandler.handleResult(resultContext);
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        // 根据返回值类型，实例化对象
        Object resultObject = createResultObject(rsw, resultMap, null);
        if (resultObject != null && !typeHandlerRegistry.hasTypeHandler(resultMap.getType())) {
            final MetaObject metaObject = configuration.newMetaObject(resultObject);
            applyAutomaticMappings(rsw, resultMap, metaObject, null);
        }
        return resultObject;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        // 得到未被处理映射的列名
        final List<String> unmappedColumnNames = rsw.getUnMappedColumnNamesMap(resultMap, columnPrefix);
        boolean foundValues = false;
        for (String columnName : unmappedColumnNames) {
            String propertyName = columnName;
            if (columnPrefix != null && !columnPrefix.isEmpty()) {
                // When columnPrefix is specified,ignore columns without the prefix.
                if (columnName.toUpperCase(Locale.ENGLISH).startsWith(columnPrefix)) {
                    propertyName = columnName.substring(columnPrefix.length());
                } else {
                    continue;
                }
            }
            // 从反射工具根据属性名称获取属性
            final String property = metaObject.findProperty(propertyName, false);
            // 这个属性有set方法
            if (property != null && metaObject.hasSetter(property)) {
                // 获取当前set方法属性类型
                final Class<?> propertyType = metaObject.getSetterType(property);
                // 根据属性类型获取对应类型处理器
                if (typeHandlerRegistry.hasTypeHandler(propertyType)) {
                    // 使用 TypeHandler 取得结果
                    final TypeHandler<?> typeHandler = rsw.getTypeHandler(propertyType, columnName);
                    // 根据jdbc的数据获取当前属性名称、类型下的数据
                    final Object value = typeHandler.getResult(rsw.getResultSet(), columnName);
                    if (value != null) {
                        foundValues = true;
                    }
                    if (value != null || propertyType.isPrimitive()) {
                        // 通过反射工具类设置属性值,假如返回某个对象，那么这个对象的set方法将被调用
                      metaObject.setValue(property, value);
                    }
                }
            }
        }
        return foundValues;
    }

    // 创建返回值对象
    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        // 定义构造方法参数类型
        final List<Class<?>> constructorArgTypes = new ArrayList<>();
        // 定义构造方法参数数据
        final List<Object> constructorArgs = new ArrayList<>();
        return createResultObject(rsw, resultMap, constructorArgTypes, constructorArgs, columnPrefix);
    }

    /**
     * 通过反射工具具体创建返回对象
     */
    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) throws SQLException {
        final Class<?> resultType = resultMap.getType();
        final MetaClass metaType = MetaClass.forClass(resultType);
        if (resultType.isInterface() || metaType.hasDefaultConstructor()) {
            // 获取普通的bean对象类型
            return objectFactory.create(resultType);
        }
        throw new RuntimeException("Do not know how to create an instance of " + resultType);
    }

}
