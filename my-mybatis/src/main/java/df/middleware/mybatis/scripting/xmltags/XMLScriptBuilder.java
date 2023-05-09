package df.middleware.mybatis.scripting.xmltags;

import df.middleware.mybatis.builder.BaseBuilder;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.scripting.defaults.RawSqlSource;
import df.middleware.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Date 2022/12/5 15:36
 * @Version 1.0
 * XML脚本构建器
 * 封装sql
 */
public class XMLScriptBuilder extends BaseBuilder {
    private Element element;
    private boolean isDynamic;
    private Class<?> parameterType;

    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }


    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    public List<SqlNode> parseDynamicTags(Element element) {
        List<SqlNode> contents = new ArrayList<>();
        // 得到sql语句文本
        String data = element.getText();
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }
}
