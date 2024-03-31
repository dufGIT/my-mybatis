package com.df.mybatis.scripting;

import com.df.mybatis.mapping.SqlSource;
import com.df.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @Author df
 * @Description: 脚本语言驱动
 * @Date 2024/3/18 15:47
 */
public interface LanguageDriver {
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);
}
