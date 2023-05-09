package df.middleware.mybatis.transaction.jdbc;

import df.middleware.mybatis.session.TransactionIsolationLevel;
import df.middleware.mybatis.transaction.Transaction;
import df.middleware.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author df
 * @Date 2022/5/26 14:27
 * @Version 1.0
 * JdbcTransaction 工厂
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
