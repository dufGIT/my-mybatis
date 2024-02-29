package com.df.mybatis.builder;

import com.df.mybatis.session.Configuration;
import com.df.mybatis.type.TypeAliasRegistry;

/**
 * @Author df
 * @Description: 构建器的基类，建造者模式
 * @Date 2024/2/7 12:33
 */
public class BaseBuilder {
    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
