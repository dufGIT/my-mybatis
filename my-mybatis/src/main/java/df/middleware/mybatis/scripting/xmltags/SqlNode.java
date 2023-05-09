package df.middleware.mybatis.scripting.xmltags;

/**
 * @Author df
 * @Date 2022/12/5 15:44
 * @Version 1.0
 * Sql节点，描述Mapper文件中配置的SQL信息
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
