package df.middleware.mybatis.executor.result;

import df.middleware.mybatis.reflection.factory.ObjectFactory;
import df.middleware.mybatis.session.ResultContext;
import df.middleware.mybatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Date 2023/3/30 12:44
 * @Version 1.0
 * 默认结果处理器
 */
public class DefaultResultHandler implements ResultHandler {
    // 存储结果上下文数据
    private final List<Object> list;

    public DefaultResultHandler() {
        this.list = new ArrayList<>();
    }
    /**
     * 通过 ObjectFactory 反射工具类，产生特定的空List
     */
    public DefaultResultHandler(ObjectFactory objectFactory) {
        this.list = objectFactory.create(List.class);
    }

    @Override
    public void handleResult(ResultContext resultContext) {
        list.add(resultContext.getResultObject());
    }
    public List<Object> getResultList() {
        return list;
    }
}
