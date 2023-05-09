package df.middleware.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author df
 * @Date 2022/5/26 11:19
 * @Version 1.0
 * 事务接口,定义获取连接，提交，回滚，关闭连接
 */
public interface Transaction {
    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
