package df.middleware.mybatis.transaction;

import df.middleware.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author df
 * @Date 2022/5/26 14:13
 * @Version 1.0
 * 事务工厂
 */
public interface TransactionFactory {

    /**
     * 根据 Connection 创建Transaction
     *
     * @param conn Existing database connection
     * @return Transaction
     */
    Transaction newTransaction(Connection conn);

    /**
     *根据数据源和事务隔离级别创建Transaction
     * @param dataSource
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
