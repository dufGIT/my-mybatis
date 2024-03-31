package com.df.mybatis.scripting.xmltags;

/**
 * @Author df
 * @Description: 静态文本SQL节点
 * @Date 2024/3/20 9:24
 */
public class StaticTextSqlNode implements SqlNode {

    private String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext dynamicContext) {
        // 将文本加入context
        dynamicContext.appendSql(text);
        return true;
    }
}
