package df.middleware.mybatis;

import df.middleware.mybatis.dao.IUserDao;
import df.middleware.mybatis.io.Resources;
import df.middleware.mybatis.po.User;
import df.middleware.mybatis.session.SqlSession;
import df.middleware.mybatis.session.SqlSessionFactory;
import df.middleware.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Author df
 * @Date 2022/12/15 9:00
 * @Version 1.0
 */
public class TestApi {
    private SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        sqlSession = sqlSessionFactory.openSession();
    }


    @org.junit.Test
    public void test_queryUserInfoById() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：基本参数
        User user = userDao.queryUserInfoById(1L);
        System.out.println("测试结果：" + user.getUserName());
    }

    @org.junit.Test
    public void test_queryUserInfo() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：对象参数
        User user = userDao.queryUserInfo(new User(1L, "10001"));
        System.out.println(user);
        System.out.println(user.getUserName());
    }


    @org.junit.Test
    public void test_updateById() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：基本参数
        userDao.updateById(1L);


        sqlSession.commit();
        //System.out.println("测试结果：" + user.getUserName());
    }

    @Test
    public void test_insertUserInfo() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证
        User user = new User();
        user.setUserId("10003");
        user.setUserName("价钱");
        user.setUserHead("1_06");
        userDao.insert(user);
        System.out.println("测试结果：" + "Insert OK");

        // 3. 提交事务
        sqlSession.commit();
    }

    @Test
    public void test_deleteUserInfoByUserId() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证
        int count = userDao.deleteUserInfoByUserId("10003");
        System.out.println("测试结果："+count);

        // 3. 提交事务
        sqlSession.commit();
    }


    @Test
    public void test_queryUserInfoList() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：对象参数
        List<User> users = userDao.queryUserInfoList();
        System.out.println(users.get(0).getUserName());
        System.out.println(users.get(1).getUserName());
        System.out.println(users.get(2).getUserName());
    }

}
