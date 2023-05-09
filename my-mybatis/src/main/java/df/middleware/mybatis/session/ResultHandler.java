package df.middleware.mybatis.session;

/**
 * @Author df
 * @Date 2022/6/13 10:54
 * @Version 1.0
 * @descript 结果处理器
 */
public interface ResultHandler {
    void handleResult(ResultContext resultContext);
}
