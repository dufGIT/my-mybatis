package com.df.mybatis.scripting.xmltags;

import com.df.mybatis.reflection.MetaObject;
import com.df.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author df
 * @Description: 动态上下文
 * @Date 2024/3/20 9:21
 */
public class DynamicContext {
    private final StringBuilder sqlBuilder = new StringBuilder();
    // TODO 未来参数化统一处理

    // 在DynamicContext的构造函数中，根据传入的参数对象是否为Map类型，有两个不同构造ContextMap的方式。
    // 而ContextMap作为一个继承了HashMap的对象，作用就是用于统一参数的访问方式：用Map接口方法来访问数据。
    // 具体来说，当传入的参数对象不是Map类型时，Mybatis会将传入的POJO对象用MetaObject对象来封装，
    // 当动态计算sql过程需要获取数据时，用Map接口的get方法包装 MetaObject对象的取值过程。
    public DynamicContext(Configuration configuration) {
        // TODO 暂不实现 N/A
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }


}
