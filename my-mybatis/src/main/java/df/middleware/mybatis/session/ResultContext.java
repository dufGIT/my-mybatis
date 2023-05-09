package df.middleware.mybatis.session;

/**
 * @Author df
 * @Date 2023/3/30 12:48
 * @Version 1.0
 * 结果上下文
 */
public interface ResultContext {
    /**
     * 获取结果
     */
    Object getResultObject();
    /**
     * 获取记录数
     */
    int getResultCount();
}
