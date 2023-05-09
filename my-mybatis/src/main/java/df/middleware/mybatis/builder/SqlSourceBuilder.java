package df.middleware.mybatis.builder;

import df.middleware.mybatis.builder.BaseBuilder;
import df.middleware.mybatis.mapping.ParameterMapping;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.parsing.GenericTokenParser;
import df.middleware.mybatis.parsing.TokenHandler;
import df.middleware.mybatis.reflection.MetaClass;
import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author df
 * @Date 2022/12/7 16:32
 * @Version 1.0
 * SQL 源构建器，此类主要完成以下两个操作
 * 1.一方面是解析Sql中的#{}占位符定义的属性，如jdbcType、javaType（使用较少）
 * 2.一方面是把#{}占位符替换成?占位符
 */
public class SqlSourceBuilder extends BaseBuilder {
    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", handler);
        String sql = genericTokenParser.parse(originSql);
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());

    }

    /**
     * ParameterMappingTokenHandler的作用是配合着GenericTokenParaser完成Mybatis的占位符#{}格式的处理。它的处理方式是将每个#{}的内容，
     * 使用?进行替换,并且将#{}里的内容转变成ParameterMapping对象。
     */
    public static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        public ParameterMapping buildParameterMapping(String content) {
            // 解析参数映射
            Map<String, String> parameterMap = new ParameterExpression(content);
            String property = parameterMap.get("property");
            Class<?> propertyType;
            if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
                propertyType = parameterType;
            } else if (property != null) {
                // 如果不是基本类型，如对象需要通过反射类解析属性处理
                MetaClass metaClass = MetaClass.forClass(parameterType);
                if (metaClass.hasGetter(property)) {
                    propertyType = metaClass.getGetterType(property);
                } else {
                    propertyType = Object.class;
                }
            } else {
                propertyType = Object.class;
            }
            System.out.println("构建参数映射 property：" + property + "propertyType：" + propertyType);
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }
    }
}
