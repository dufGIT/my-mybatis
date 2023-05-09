package df.middleware.mybatis.builder.xml;

import df.middleware.mybatis.builder.BaseBuilder;
import df.middleware.mybatis.datasource.DataSourceFactory;
import df.middleware.mybatis.datasource.pooled.PooledDataSource;
import df.middleware.mybatis.io.Resources;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.Environment;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.SqlCommandType;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.transaction.TransactionFactory;
import df.middleware.mybatis.type.TypeAliasRegistry;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// todo 第四章
// xml配置构建器，建造者模式，继承BaseBuilder
// 对xml里的配置进行解析，，如mapper, select等等
public class XMLConfigBuilder extends BaseBuilder {
    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 调用父类初始化Configuration，因为xml解析完毕需要通过config类将代理类注册进去
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置:类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     */
    public Configuration parse() {
        try {
            // 环境
            environmentsElement(root.element("environments"));
            // 解析映射器
            mapperElement(root.element("mappers"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return configuration;
    }

    /**
     * <environments default="development">
     * <environment id="development">
     * <transactionManager type="JDBC">
     * <property name="..." value="..."/>
     * </transactionManager>
     * <dataSource type="DRUID">
     * <property name="driver" value="${driver}"/>
     * <property name="url" value="${url}"/>
     * <property name="username" value="${username}"/>
     * <property name="password" value="${password}"/>
     * </dataSource>
     * </environment>
     * </environments>
     */
    private void environmentsElement(Element context) throws Exception {
        String enviroment = context.attributeValue("default");
        List<Element> enviromentList = context.elements("environment");
        for (Element e : enviromentList) {
            String id = e.attributeValue("id");
            if (enviroment.equals(id)) {
                // 数据源
                Element dataSourceElement = e.element("dataSource");
                // 从类型注册机里找到DRUID名字的并得到类DataSourceFactory
                // 如果设置POOLED就是有池化的一个处理
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                // 获取xml中数据源的属性数据
                List<Element> propertiesList = dataSourceElement.elements("property");
                Properties properties = new Properties();

                for (Element prop : propertiesList) {
                    properties.setProperty(prop.attributeValue("name"), prop.attributeValue("value"));
                }
                // 设置数据源属性对象
                dataSourceFactory.setProperties(properties);
                // 设置DruidDataSource|| PooledDataSource || UnPooledDataSource数据源并返回
                DataSource dataSource = dataSourceFactory.getDataSource();
                // 找到类型注册机里JDBC事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id).dataSource(dataSource)
                        .transactionFactory(txFactory);

                // 将环境信息存储到配置里供其他需要的地方使用
                configuration.setEnvironment(environmentBuilder.Builder());
            }
        }
    }

    /*
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     * </mappers>
     */
    // 将xml中的配置解析出来存储到对应的实体类中
    // 处理mapper的方法，mapper里有多个sql语句，所以需要List
    private void mapperElement(Element mappers) throws Exception {
        // 得到mybatis-config-datasource.xml的mappers标签里的mapper
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();
        }
    }

    // mapper namespace
    //  sql语句，参数，返回值
    //  sqlCommandType，BoundSql,MapperStatement
    // 添加mapperstatement，addMapper
}
