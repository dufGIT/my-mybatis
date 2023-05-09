package df.middleware.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/13 14:26
 * @Version 1.0
 * @description 结果集处理器
 */
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement statement) throws SQLException;
}
