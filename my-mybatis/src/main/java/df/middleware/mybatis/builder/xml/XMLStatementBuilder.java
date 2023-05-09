package df.middleware.mybatis.builder.xml;

import df.middleware.mybatis.builder.BaseBuilder;
import df.middleware.mybatis.builder.MapperBuilderAssistant;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.mapping.SqlCommandType;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.scripting.LanguageDriver;
import df.middleware.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.Locale;

/**
 * @Author df
 * @Date 2022/11/19 10:18
 * @Version 1.0
 * XML语句构建器
 */
public class XMLStatementBuilder extends BaseBuilder {
    private MapperBuilderAssistant builderAssistant;
    private Element element;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, Element element) {
        super(configuration);
        this.element = element;
        this.builderAssistant = builderAssistant;
    }

    // 开始解析select标签的id，参数，返回值，并调用语言驱动器
    public void parseStatementNode() {
        String id = element.attributeValue("id");
        // 参数类型
        String parameterType = element.attributeValue("parameterType");
        // 获取参数类型
        Class<?> parameterTypeClass = resolveAlias(parameterType);
        // 外部应用resultMap
        String resultMap = element.attributeValue("resultMap");
        // 结果
        String resultType = element.attributeValue("resultType");
        // 获取结果类型
        Class<?> resultTypeClass = resolveAlias(resultType);
        // 获取命令类型(select|insert|update|delete)
        String nodeName = element.getName();

        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));


        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);

        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);

        // 调用MapperBuilderAssistant助手类帮助完成mapper的绑定映射
        builderAssistant.addMapperStatement(
                id,
                sqlSource,
                sqlCommandType,
                parameterTypeClass,
                resultMap,
                resultTypeClass,
                langDriver);

    }
}
