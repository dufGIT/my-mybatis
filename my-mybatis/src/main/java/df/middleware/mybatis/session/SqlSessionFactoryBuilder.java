package df.middleware.mybatis.session;

import df.middleware.mybatis.builder.xml.XMLConfigBuilder;
import df.middleware.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

// todo 第四节加
// mybatis的入口
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
}
