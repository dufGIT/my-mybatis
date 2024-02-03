package com.df.mybatis.test.dao;

/**
 * @Author df
 * @Description: dao接口
 * @Date 2024/1/24 11:31
 */
public interface IUserDao {
    String queryUserName(String uId);

    String queryUserAge(String uId);
}
