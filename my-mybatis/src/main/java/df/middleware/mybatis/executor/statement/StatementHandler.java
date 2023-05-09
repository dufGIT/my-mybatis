package df.middleware.mybatis.executor.statement;

import df.middleware.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/13 11:44
 * @Version 1.0
 * @Descript 语句处理器
 */
public interface StatementHandler {

    /**
     * 准备语句
     */
    Statement prepare(Connection connection);

    /**
     * 参数化
     */
    void parameterize(Statement statement) throws SQLException;

    /**
     * 执行查询
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

    int update(Statement statement) throws SQLException;
}
