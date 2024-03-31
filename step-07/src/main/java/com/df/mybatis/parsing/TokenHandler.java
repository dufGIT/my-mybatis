package com.df.mybatis.parsing;

/**
 * @Author df
 * @Description: 记号处理器
 * @Date 2024/3/20 16:20
 */
public interface TokenHandler {
    String handleToken(String content);
}
