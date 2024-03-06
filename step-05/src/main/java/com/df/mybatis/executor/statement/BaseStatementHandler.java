package com.df.mybatis.executor.statement;

import com.df.mybatis.executor.Executor;
import com.df.mybatis.executor.resultset.ResultSetHandler;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author df
 * @Description: 语句处理器抽象基类
 * @Date 2024/3/1 13:54
 */
public abstract class BaseStatementHandler implements StatementHandler{
    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置，可以被抽取，提供配置
            // 设置查询超时时间
            statement.setQueryTimeout(350);
            // 控制每批检索的行数
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

}
