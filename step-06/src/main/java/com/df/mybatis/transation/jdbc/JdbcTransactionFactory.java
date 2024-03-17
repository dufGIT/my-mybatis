package com.df.mybatis.transation.jdbc;

import com.df.mybatis.session.TransactionIsolationLevel;
import com.df.mybatis.transation.Transaction;
import com.df.mybatis.transation.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author df
 * @Description: JdbcTransaction 工厂
 * @Date 2024/2/16 15:20
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
