package df.middleware.mybatis.session;

// SqlSessionFactory的工厂，获取sqlSesion类
public interface SqlSessionFactory {
    // 打开一个 session
    SqlSession openSession();
}
