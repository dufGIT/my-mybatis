package df.middleware.mybatis.dao;

import df.middleware.mybatis.po.User;

import java.util.List;

public interface IUserDao {
    User queryUserInfoById(long id);

    User queryUserInfo(User user);

    void updateById(long id);

    List<User> queryUserInfoList();

    void insert(User req);

    int deleteUserInfoByUserId(String userId);
}
