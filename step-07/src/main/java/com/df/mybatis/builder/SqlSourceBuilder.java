package com.df.mybatis.builder;

import com.df.mybatis.mapping.ParameterMapping;
import com.df.mybatis.mapping.SqlSource;
import com.df.mybatis.parsing.GenericTokenParser;
import com.df.mybatis.parsing.TokenHandler;
import com.df.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author df
 * @Description: SQL 源码构建器
 * @Date 2024/3/20 15:45
 */
public class SqlSourceBuilder extends BaseBuilder {

    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    /**
     * 处理sql语句
     */
    public SqlSource parse(String originalSql, Class<?> parameterType) {
        // 参数处理
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        // 拿到了处理过后的语句
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
    }


    /**
     * 此类参数映射词语处理器，实现TokenHandler，最终将参数转换成 ?
     */
    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();
        private Class<?> parameterType;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType) {
            super(configuration);
            this.parameterType = parameterType;
        }

        // 构建参数映射
        private ParameterMapping buildParameterMapping(String content) {
            // 先解析参数映射,就是转化成一个 HashMap | #{favouriteSection,jdbcType=VARCHAR}
            // 根据Sql语句信息查找解析，参数属性,javaType,jdbcType
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");
            Class<?> propertyType = parameterType;
            // 构建参数映射
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }
    }
}
