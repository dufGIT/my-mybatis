package df.middleware.mybatis.executor.statement;

import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.ResultHandler;
import df.middleware.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/13 14:07
 * @Version 1.0
 * @description 预处理语句处理器（PREPARED）
 */
public class PreparedStatementHandler extends BaseStatementHandler {


    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultSetHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, rowBounds, resultSetHandler, boundSql);
    }

    // 初始化参数
    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        // PreparedStatement是预编译的,对于批量处理可以大大提高效率
        // update 表 字段=值 where 字段=?
        return connection.prepareStatement(sql);
    }

    // 参数设置
    @Override
    public void parameterize(Statement statement) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }

    // 执行查询并对结果进行封装
    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.<E>handleResultSets(ps);
    }

    @Override
    public int update(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return ps.getUpdateCount();
    }
}
