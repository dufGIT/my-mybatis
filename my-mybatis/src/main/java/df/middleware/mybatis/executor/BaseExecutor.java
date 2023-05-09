package df.middleware.mybatis.executor;

import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;
import df.middleware.mybatis.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/13 11:04
 * @Version 1.0
 * @description 基础执行器，处理事务和执行查询的定义，此类处理共性的方法
 */
public abstract class BaseExecutor implements Executor {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }

    @Override
    public int update(MappedStatement ms, Object parameter)throws SQLException{
         return doUpdate(ms,parameter);
    }

    protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter,  RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameter,  rowBounds, resultHandler, boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql);

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    protected void closeStatement(Statement statement){
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }
}
