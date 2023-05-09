package df.middleware.mybatis.executor;

import df.middleware.mybatis.executor.statement.StatementHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;
import df.middleware.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/13 11:36
 * @Version 1.0
 * @Descript 简单执行器
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            // 调用创建语句处理器-PreparedStatementHandler
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
            Connection connection = transaction.getConnection();
            // 调用语句处理器-准备操作，如初始化参数
            stmt = handler.prepare(connection);
            // 设置参数
            handler.parameterize(stmt);
            // 调用语句处理器的更新方法
            return handler.update(stmt);

        } finally {
            closeStatement(stmt);
        }

    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            // 调用创建语句处理器-PreparedStatementHandler
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, rowBounds, resultHandler, boundSql);
            Connection connection = transaction.getConnection();
            // 调用语句处理器-准备操作，如初始化参数
            Statement stmt = handler.prepare(connection);
            // 设置参数
            handler.parameterize(stmt);
            // 调用语句处理器的查询方法
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
