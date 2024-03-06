package com.df.mybatis.executor.statement;

import com.df.mybatis.executor.Executor;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Description: 简单语句处理器（STATEMENT）
 * @Date 2024/3/1 13:58
 */
public class SimpleStatementHandler  extends BaseStatementHandler{

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        // SimpleStatement不处理参数
        // N/A
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        String sql = boundSql.getSql();
        // 执行sql语句
        statement.execute(sql);
        // 返回结果
        return resultSetHandler.handleResultSets(statement);
    }
}
