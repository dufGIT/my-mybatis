package df.middleware.mybatis.builder.xml;

import df.middleware.mybatis.builder.BaseBuilder;
import df.middleware.mybatis.builder.MapperBuilderAssistant;
import df.middleware.mybatis.io.Resources;
import df.middleware.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;


/**
 * @Author df
 * @Date 2022/11/19 9:57
 * @Version 1.0
 * Xml映射构建器
 */
public class XMLMapperBuilder extends BaseBuilder {

    private Element element;
    private String resource;
    // 映射器构建助手
    private MapperBuilderAssistant builderAssistant;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    public XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
        // 创建构建mapper助手类
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
    }

    /**
     * 解析
     * 绑定namespace
     * 找到mapper下的所有select标签
     */
    public void parse() throws ClassNotFoundException {
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(element);
            // 标记resource已被加载过
            configuration.addLoadedResource(resource);
            // 绑定映射器nameSpace
            configuration.addMapper(Resources.classForName(builderAssistant.getCurrentNamespace()));
        }
    }

    // 配置mapper元素
    // <mapper namespace="org.mybatis.example.BlogMapper">
    //   <select id="selectBlog" parameterType="int" resultType="Blog">
    //    select * from Blog where id = #{id}
    //   </select>
    // </mapper>
    // 找到mapper的namespace以及select的sql语句
    private void configurationElement(Element element) {
        // 配置namespace
        String namespace = element.attributeValue("namespace");
        if (namespace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }
        builderAssistant.setCurrentNamespace(namespace);
        // select
        buildStatementFromContext(element.elements("select"),
                element.elements("update"),
                element.elements("insert"),
                element.elements("delete"));
    }

    // 配置select|insert|update|delete
    private void buildStatementFromContext(List<Element>... lists) {
        for(List<Element> list:lists){
            for (Element element : list) {
                // 解析语句
                final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, element);
                statementParser.parseStatementNode();
            }
        }
    }

}
