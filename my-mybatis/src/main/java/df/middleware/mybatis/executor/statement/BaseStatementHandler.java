package df.middleware.mybatis.executor.statement;

import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.executor.parameter.ParameterHandler;
import df.middleware.mybatis.executor.resultset.ResultSetHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author df
 * @Date 2022/6/13 13:52
 * @Version 1.0
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;
    protected final BoundSql boundSql;
    protected final RowBounds rowBounds;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.rowBounds = rowBounds;

        // 新增判断，update不传参数
        if (boundSql == null) {
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }
        this.parameterHandler = configuration.newParameterHandler(mappedStatement, parameterObject, boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, rowBounds, resultHandler, boundSql);
        this.boundSql = boundSql;
    }

    @Override
    public Statement prepare(Connection connection) {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing statement.  Cause: " + e, e);
        }
    }

    // 定义实例化抽象方法
    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
