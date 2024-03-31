package com.df.mybatis.session.defaults;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.executor.Executor;
import com.df.mybatis.mapping.Environment;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;
import com.df.mybatis.session.SqlSessionFactory;
import com.df.mybatis.session.TransactionIsolationLevel;
import com.df.mybatis.transation.Transaction;
import com.df.mybatis.transation.TransactionFactory;

import java.sql.SQLException;

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
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
