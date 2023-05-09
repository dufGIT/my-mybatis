package df.middleware.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author df
 * @Date 2023/3/14 12:43
 * @Version 1.0
 */
public interface ParameterHandler {
    /**
     * 获取参数
     */
    Object getParameterObject();

    void setParameters(PreparedStatement ps) throws SQLException;
}
