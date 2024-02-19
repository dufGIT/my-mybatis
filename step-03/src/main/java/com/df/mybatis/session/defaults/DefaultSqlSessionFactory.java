package com.df.mybatis.session.defaults;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;
import com.df.mybatis.session.SqlSessionFactory;

/**
 * @Author df
 * @Description: SqlSession工厂类
 * @Date 2024/2/2 16:54
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
