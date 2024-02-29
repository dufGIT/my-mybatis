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
        for (int i = 0; i < 30; i++) {
            User user = userDao.queryUserInfoById(1L);
            logger.info("测试结果：{}", JSON.toJSONString(user));
        }
    }

    // 测试池子
    @Test
    public void test_pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("root");

        // 下面测试循环获取连接并依次关闭就可以测试到，第一次获取连接时创建连接放入活跃池子中，
        // connection.close()关闭此连接时就是回收此连接，将连接放入空闲链接列表里，如果空闲链接列表满了就直接关闭此链接就可以了
        // 然后循环获取连接就直接从空闲列表里取就好了，一直这样循环。

        // 下面测试循环获取连接并不关闭就可以测试到，第一次获取连接时创建连接放入活跃池子中，
        // 然后就一直创建连接，直到活跃的池子满了，需要阻塞等待一定时间后继续走循环，直到连接超时，将活跃池子中的连接池取出移除，接着复用就可以了

        // 持续获得链接
        while (true){
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            Thread.sleep(1000);
            //connection.close();
        }
    }



}
