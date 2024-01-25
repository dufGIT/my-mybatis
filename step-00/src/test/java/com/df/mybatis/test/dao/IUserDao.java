package com.df.mybatis.test.dao;

/**
 * @Author df
 * @Description: TODO
 * @Date 2024/1/24 11:31
 */
public interface IUserDao {
    String queryUserName(String uId);

    Integer queryUserAge(String uId);
}
