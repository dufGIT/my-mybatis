package df.middleware.mybatis.session.defaults;

import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.mapping.Environment;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.SqlSession;
import df.middleware.mybatis.session.SqlSessionFactory;
import df.middleware.mybatis.session.TransactionIsolationLevel;
import df.middleware.mybatis.transaction.Transaction;
import df.middleware.mybatis.transaction.TransactionFactory;

// sqlSession工厂实现类
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        final Environment environment = configuration.getEnvironment();
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
        // 创建执行器
        final Executor executor=configuration.newExecutor(tx);
        return new DefaultSqlSession(configuration,executor);
    }
}
