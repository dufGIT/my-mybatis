package df.middleware.mybatis.builder;

import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.type.TypeAliasRegistry;
import df.middleware.mybatis.type.TypeHandlerRegistry;

// todo 第四章加   构建器的基类，建造者模式

/**
 * 建造者模式：通过将多个简单对象通过一步步的组装构建出一个复杂对象的过程。
 * 将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示
 */
// 构造器基类
public class BaseBuilder {

    // 配置
    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }

    // 获取configuration
    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
