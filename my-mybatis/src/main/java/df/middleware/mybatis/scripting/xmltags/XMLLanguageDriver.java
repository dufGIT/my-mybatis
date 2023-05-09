package df.middleware.mybatis.scripting.xmltags;

import df.middleware.mybatis.executor.parameter.ParameterHandler;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.scripting.LanguageDriver;
import df.middleware.mybatis.scripting.defaults.DefaultParameterHandler;
import df.middleware.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @Author df
 * @Date 2022/12/5 14:28
 * @Version 1.0
 * XML语言驱动器
 */
public class XMLLanguageDriver implements LanguageDriver {
    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建解析
        XMLScriptBuilder xmlScriptBuilder = new XMLScriptBuilder(configuration, script, parameterType);
        return xmlScriptBuilder.parseScriptNode();
    }
}
