package com.df.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.builder.xml.XMLConfigBuilder;
import com.df.mybatis.io.Resources;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;
import com.df.mybatis.session.SqlSessionFactory;
import com.df.mybatis.session.SqlSessionFactoryBuilder;
import com.df.mybatis.session.defaults.DefaultSqlSession;
import com.df.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.df.mybatis.test.dao.IUserDao;
import com.df.mybatis.test.po.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * @author df
 * @description 单元测试
 * @date 2022/3/26
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

//    @Test
//    public void test_SqlSessionFactory() throws IOException {
//        // 1. 从SqlSessionFactory中获取SqlSession
//        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//
//        // 2. 获取映射器对象
//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//
//        // 3. 测试验证
//        String res = userDao.queryUserInfoById("10001");
//        logger.info("测试结果：{}", res);
//    }



    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

    @Test
    public void test_selectOne() throws IOException {
        // 解析 XML
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();

        // 获取 DefaultSqlSession
        SqlSession sqlSession = new DefaultSqlSession(configuration);

        // 执行查询：默认是一个集合参数
        Object[] req = {1L};
        Object res = sqlSession.selectOne("com.df.mybatis.test.dao.IUserDao.queryUserInfoById", req);
        logger.info("测试结果：{}", JSON.toJSONString(res));
    }


}
