package com.df.mybatis.scripting.xmltags;

import com.df.mybatis.mapping.SqlSource;
import com.df.mybatis.scripting.LanguageDriver;
import com.df.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @Author df
 * @Description: XML语言驱动器
 * @Date 2024/3/20 9:16
 */
public class XMLLanguageDriver implements LanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }
}
