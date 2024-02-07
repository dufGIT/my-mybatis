package com.df.mybatis.session.defaults;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;

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
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
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
