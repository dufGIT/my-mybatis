package df.middleware.mybatis.scripting.xmltags;

import java.util.List;

/**
 * @Author df
 * @Date 2022/12/5 15:50
 * @Version 1.0
 * 混合Sql节点，最终由MixedSqlNode将所有的SqlNode实现串起来执行，也看作责任链模式
 * 1.将一组SqlNode对象进行串联执行，通常多个SqlNode对象才能联合表述一个SQL信息，所以就需要借助MixedSqlNode来将其进行串联，最终形成一个完整的SQL信息
 *
 * 责任链模式：很多对象由每一个对象对其下家的引用而连接起来形成一条链条，请求在这链条上传递
 * 直到链上的某一个对象决定处理此请求。发出这个请求的客户端并不知道链上的哪一个对象最终处理这个请求，这使得系统可以在不影响客户端的情况下动态地重新组织和分配责任。
 */
public class MixedSqlNode implements SqlNode {
    //组合模式，拥有一个SqlNode的List
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        // 依次调用list里每个元素的apply
        contents.forEach(node -> node.apply(context));
        return true;
    }
}
