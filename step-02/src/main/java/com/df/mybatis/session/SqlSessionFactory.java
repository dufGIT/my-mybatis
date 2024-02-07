package com.df.mybatis.session;

/**
 * @Author df
 * @Description: SqlSessionFactory工厂类
 * @Date 2024/2/2 16:33
 */
public interface SqlSessionFactory {
    /**
     * 打开一个 session
     * @return SqlSession
     */
    SqlSession openSession();

}
