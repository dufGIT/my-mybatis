package com.df.mybatis.session;

import com.df.mybatis.builder.xml.XMLConfigBuilder;
import com.df.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @Author df
 * @Description: 构建SqlSessionFactory的工厂
 * @Date 2024/2/7 13:13
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }


}
