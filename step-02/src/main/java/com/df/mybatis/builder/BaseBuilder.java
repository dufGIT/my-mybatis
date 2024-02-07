package com.df.mybatis.builder;

import com.df.mybatis.session.Configuration;

/**
 * @Author df
 * @Description: 构建器的基类，建造者模式
 * @Date 2024/2/7 12:33
 */
public class BaseBuilder {
    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
