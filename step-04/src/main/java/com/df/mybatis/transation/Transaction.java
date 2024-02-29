package com.df.mybatis.transation;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author df
 * @Description: 事务接口
 * @Date 2024/2/16 15:00
 */
public interface Transaction {
    Connection getConnection() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    void close() throws SQLException;
}
