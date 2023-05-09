package df.middleware.mybatis.dao;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import df.middleware.mybatis.binding.MapperProxyFactory;
import df.middleware.mybatis.binding.MapperRegistry;
import df.middleware.mybatis.io.Resources;
import df.middleware.mybatis.po.User;
import df.middleware.mybatis.session.SqlSession;
import df.middleware.mybatis.session.SqlSessionFactory;
import df.middleware.mybatis.session.SqlSessionFactoryBuilder;
import df.middleware.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


public class TestAop {
    // 最简单的mybatis
    /*@Test
    public void test_MapperProxyFactory() {
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession = new HashMap<>();

        sqlSession.put("df.middleware.mybatis.dao.IUserDao.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        sqlSession.put("df.middleware.mybatis.dao.IUserDao.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");
        IUserDao userDao = factory.newInstance(sqlSession);

        String res = userDao.queryUserName("10001");
        System.out.println(res);
        //logger.info("测试结果：{}", res);
    }*/


  /*  @Test
    public void test_MapperProxyFactory1() {
        // 1.注册Mapper
        // 过程，将当前类路径传输进去通过工具扫描找到类，判断当前要代理的类是接口并且没有被代理过
        // 就将代理工厂存储到容器里
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("df.middleware.mybatis.dao.IUserDao");

        // 从SqlSession工厂获取Seesion
        // SqlSessionFactory是接口， DefaultSqlSessionFactory是其的实现类
        // SqlSession是接口，DefaultSqlSession是其的实现类
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获取代理对象
        IUserDao iUserDao = sqlSession.getMapper(IUserDao.class);
        // 4. 测试验证
        String res = iUserDao.queryUserName("10001");
        System.out.println(res);
    }*/




    /**
     * 测试解析xml的Sql语句和数据源信息
     * 1.配置xml数据源和sql一些信息
     * 2.将解析信息根据不同的功能模块进行存放，数据源放入dataSourceFactory，
     * 事务存储在事务范围内，sql语句放入Mapperstatement等等，并把namespace
     * 存入代理类供后续代理
     * 3.将配置的dao类添加代理容器中，获取时进行实例代理得到代理对象，当代理对象
     * 调用方法时，则进行invoke方法调用，此时则处理自己业务开始调用sqlSession的selectOne
  * * */
    @Test
    public void testA() throws IOException {

        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        IUserDao iUserDao = sqlSession.getMapper(IUserDao.class);


           // User user = iUserDao.queryUserInfoById("10001");
           // JSONObject json = JSONUtil.parseObj(user, false, true);
           // System.out.println(json);



    }

}
