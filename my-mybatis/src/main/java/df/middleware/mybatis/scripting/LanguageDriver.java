package df.middleware.mybatis.scripting;

import df.middleware.mybatis.executor.parameter.ParameterHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @Author df
 * @Date 2022/12/5 14:16
 * @Version 1.0
 * 脚本语言驱动
 */
public interface LanguageDriver {
    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);
}
