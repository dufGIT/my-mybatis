package df.middleware.mybatis.parsing;

/**
 * @Author df
 * @Date 2022/12/7 16:52
 * @Version 1.0
 * 记号处理器，处理占位符号
 */
public interface TokenHandler {
    String handleToken(String content);
}
