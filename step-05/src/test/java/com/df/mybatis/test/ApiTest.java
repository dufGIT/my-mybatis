package com.df.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.builder.xml.XMLConfigBuilder;
import com.df.mybatis.datasource.pooled.PooledDataSource;
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
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author df
 * @description 单元测试
 * @date 2022/3/26
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

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
}
