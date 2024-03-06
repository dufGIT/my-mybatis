package com.df.mybatis.executor;

import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.ResultHandler;
import com.df.mybatis.transation.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author df
 * @Description: 执行器
 * @Date 2024/3/1 13:12
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
