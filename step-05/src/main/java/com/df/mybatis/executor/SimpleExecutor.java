package com.df.mybatis.executor;

import com.df.mybatis.executor.statement.StatementHandler;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.ResultHandler;
import com.df.mybatis.transation.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Description: 简单执行器
 * @Date 2024/3/1 13:44
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
            Connection connection = transaction.getConnection();
            Statement stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
