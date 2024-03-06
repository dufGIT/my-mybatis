package com.df.mybatis.test.dao;

import com.df.mybatis.test.po.User;

/**
 * @author df
 * @description
 * @date 2022/3/26
 */
public interface IUserDao {

    User queryUserInfoById(Long uId);

}
