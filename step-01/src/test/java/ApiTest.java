import com.df.mybatis.binding.MapperProxyFactory;
import com.df.mybatis.test.dao.IUserDao;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author df
 * @Description: TODO
 * @Date 2024/1/24 11:29
 */
public class ApiTest {
    @Test
    public void test_MapperProxyFactory() {
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession=new HashMap<>();
        sqlSession.put("com.df.mybatis.test.dao.IUserDao.queryUserName","模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        sqlSession.put("com.df.mybatis.test.dao.IUserDao.queryUserAge","模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");

        IUserDao userDao= factory.newInstance(sqlSession);
        String res = userDao.queryUserName("10001");
        System.out.println("测试结果："+ res);

        String res1 = userDao.queryUserAge("10001");
        System.out.println("测试结果："+ res1);

    }
}

