package com.df.mybatis.session.defaults;

import com.df.mybatis.executor.Executor;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author df
 * @Description: 会话实现
 * @Date 2024/2/2 16:59
 */
public class DefaultSqlSession implements SqlSession {

    /**
     * 结合配置项获取信息
     */
    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        try {
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            List<T> list = executor.query(mappedStatement, parameter, Executor.NO_RESULT_HANDLER, mappedStatement.getBoundSql());
            return list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    // MapperMethod里获取MappedStatement里使用
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }


}
