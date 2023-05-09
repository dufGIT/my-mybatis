package df.middleware.mybatis.scripting.xmltags;

import df.middleware.mybatis.reflection.MetaObject;
import df.middleware.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author df
 * @Date 2022/12/5 15:44
 * @Version 1.0
 */
public class  DynamicContext {
    // 在编写映射文件时, '${parameter}','${databaseId}'分别可以取到当前用户传入的参数, 以及当前执行的数据库类型
    public static final String PARAMETER_OBJECT_KEY = "_parameter";
    // _databaseId可以指定不同的数据库支持
    public static final String DATABASE_ID_KEY = "_databaseId";
    private final StringBuilder sqlBuilder = new StringBuilder();
    private final ContextMap bindings;
    // 构造函数, 对传入的parameterObject对象进行“map”化处理;
    // 也就是说，你传入的pojo对象，会被当作一个键值对数据来源来进行处理，读取这个pojo对象的接口,依然是Map对象(依然是以Map接口方式来进行读取)。
    public DynamicContext(Configuration configuration, Object parameterObject) {
        /*
         * 在DynamicContext的构造函数中，可以看到:
         *    1. 根据传入的参数对象是否为Map类型，有两个不同构造ContextMap的方式。
         *    2. 而ContextMap作为一个继承了HashMap的对象，作用就是用于统一参数的访问方式：用Map接口方法来访问数据。具体来说:
         *    	   2.1 当传入的参数对象不是Map类型时，Mybatis会将传入的POJO对象用MetaObject对象来封装，
         *         2.2 当动态计算sql过程需要获取数据时，用Map接口的get方法包装 MetaObject对象的取值过程。
         *         2.3 ContextMap覆写的get方法正是为了上述目的.具体参见下面的`ContextMap`覆写的get方法里的详细解释.
         */
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {
            bindings = new ContextMap(null);
        }
        // 向刚构造出来的ContextMap实例中推入用户本次传入的参数parameterObject.
        bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
        // 向刚构造出来的ContextMap实例中推入用户配置的DatabaseId.
        bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId());
    }

    // 追加Sql
    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    // 获取Sql语句
    public String getSql() {
        return sqlBuilder.toString().trim();
    }

    // 上下文map，静态内部类
    static class ContextMap extends HashMap<String, Object> {
        private MetaObject parameterMetaObject;
        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }
    }
}
