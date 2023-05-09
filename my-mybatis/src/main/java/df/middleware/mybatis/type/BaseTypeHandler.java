package df.middleware.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author df
 * @Date 2023/3/13 12:39
 * @Version 1.0
 * 类型处理器基类，模板模式
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        setNonNullParameter(ps, i, parameter, jdbcType);
    }

    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;
}
