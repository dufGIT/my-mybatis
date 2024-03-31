package com.df.mybatis.scripting.xmltags;

/**
 * @Author df
 * @Description: SQL 节点
 * @Date 2024/3/20 9:21
 */
public interface SqlNode {
    boolean apply(DynamicContext dynamicContext);
}
