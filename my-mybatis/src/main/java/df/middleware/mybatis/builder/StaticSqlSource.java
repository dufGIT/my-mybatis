package df.middleware.mybatis.builder;

import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.ParameterMapping;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.SqlSession;

import java.util.List;

/**
 * @Author df
 * @Date 2022/12/14 17:41
 * @Version 1.0
 * 主要创建BoundSql，供其他的SqlSource实现类使用，一个中间状态
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
