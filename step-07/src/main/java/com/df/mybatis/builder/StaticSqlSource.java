package com.df.mybatis.builder;

import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.ParameterMapping;
import com.df.mybatis.mapping.SqlSource;
import com.df.mybatis.session.Configuration;

import java.util.List;

/**
 * @Author df
 * @Description: 静态SQL源码
 * @Date 2024/3/20 16:26
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }
}
