package com.df.mybatis.scripting.xmltags;

import java.util.List;

/**
 * @Author df
 * @Description: 混合SQL节点
 * @Date 2024/3/20 9:27
 */
public class MixedSqlNode implements SqlNode {

    // 组合模式，动态的或静态的Sql都可以放入
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext dynamicContext) {
        contents.forEach(node -> node.apply(dynamicContext));
        return true;
    }
}
