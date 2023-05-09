package df.middleware.mybatis.scripting.defaults;

import df.middleware.mybatis.builder.SqlSourceBuilder;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.SqlSource;
import df.middleware.mybatis.scripting.xmltags.DynamicContext;
import df.middleware.mybatis.scripting.xmltags.SqlNode;
import df.middleware.mybatis.session.Configuration;

import java.util.HashMap;

/**
 * @Author df
 * @Date 2022/12/5 15:52
 * @Version 1.0
 * 原始SQL源码，比 DynamicSqlSource 动态SQL处理快
 * 存储的是只有“#{}”或者没有标签的纯文本sql信息
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    // 数据sql解析
    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        // Sql源构建器
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        // 解析最终可执行的Sql
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        // 获取已绑定过的Sql
        return sqlSource.getBoundSql(parameterObject);
    }

    // 获取Sql
    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext dynamicContext = new DynamicContext(configuration, null);
        // 将Sql信息存入dynamicContext的sqlBuilder里
        rootSqlNode.apply(dynamicContext);
        // 从dynamicContext的sqlBuilder里得到Sql文本
        return dynamicContext.getSql();
    }
}
