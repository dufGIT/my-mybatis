package com.df.mybatis.mapping;

/**
 * @Author df
 * @Description: SQL源码
 * @Date 2024/3/18 15:03
 */
public interface SqlSource {
    BoundSql getBoundSql(Object parameterObject);
}
