package com.df.mybatis.scripting.defaults;

import com.df.mybatis.builder.SqlSourceBuilder;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.SqlSource;
import com.df.mybatis.scripting.xmltags.DynamicContext;
import com.df.mybatis.scripting.xmltags.SqlNode;
import com.df.mybatis.session.Configuration;

/**
 * @Author df
 * @Description: 原始SQL源码，比 DynamicSqlSource 动态SQL处理快
 * @Date 2024/3/20 9:31
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    /**
     * 处理sql解析，得到处理过后可运行的sql
     **/
    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        // sql源构建类
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        // 解析sql,暂时拿到静态Sql源
        sqlSource = sqlSourceBuilder.parse(sql, clazz);
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    /**
     * 获取没有处理完的SQl语句
     */
    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration);
        // 往DynamicContext的局部变量里放数据
        rootSqlNode.apply(context);
        // 放完去取sql
        return context.getSql();
    }


}
