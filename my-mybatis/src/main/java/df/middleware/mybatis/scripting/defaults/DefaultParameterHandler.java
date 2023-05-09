package df.middleware.mybatis.scripting.defaults;

import df.middleware.mybatis.executor.parameter.ParameterHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.ParameterMapping;
import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.type.JdbcType;
import df.middleware.mybatis.type.TypeHandler;
import df.middleware.mybatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author df
 * @Date 2023/3/14 12:45
 * @Version 1.0
 * 默认参数处理器
 */
public class DefaultParameterHandler implements ParameterHandler {
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
        this.configuration = mappedStatement.getConfiguration();
    }


    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (null != parameterMappings) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();
                Object value;
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();
                // 设置参数
                System.out.println("根据每个ParameterMapping中的TypeHandler设置对应的参数信息 value：" + value);
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, i + 1, value, jdbcType);
            }
        }
    }
}
