package df.middleware.mybatis.mapping;

/**
 * @Author df
 * @Date 2022/12/5 14:24
 * @Version 1.0
 * SQL源,主要作用创建一个Sql语句
 */
public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
