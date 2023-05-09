package df.middleware.mybatis.mapping;

import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.type.JdbcType;
import df.middleware.mybatis.type.TypeHandler;

/**
 * @Author df
 * @Date 2023/3/28 12:38
 * @Version 1.0
 * 结果映射的每一个字段的信息
 */
public class ResultMapping {
    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    ResultMapping() {

    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();
    }
}
