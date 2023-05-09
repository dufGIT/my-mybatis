package df.middleware.mybatis.executor;

import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;
import df.middleware.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/12 19:07
 * @Version 1.0
 * @description 执行器
 */
public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    // 定义执行Sql查询操作
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    int update(MappedStatement ms, Object parameter)throws SQLException;

    Transaction getTransaction();

    // 以下事务处理-提交、回滚、关闭
    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
