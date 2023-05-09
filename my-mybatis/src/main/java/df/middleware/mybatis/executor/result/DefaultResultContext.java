package df.middleware.mybatis.executor.result;

import df.middleware.mybatis.session.ResultContext;

/**
 * @Author df
 * @Date 2023/3/30 12:52
 * @Version 1.0
 * 默认结果上下文
 */
public class DefaultResultContext implements ResultContext {
    // 结果数据对象
    private Object resultObject;
    private int resultCount;

    public DefaultResultContext() {
        this.resultObject = null;
        this.resultCount = 0;
    }

    @Override
    public Object getResultObject() {
        return resultObject;
    }

    @Override
    public int getResultCount() {
        return resultCount;
    }

    public void nextResultObject(Object resultObject) {
        resultCount++;
        this.resultObject = resultObject;
    }
}
