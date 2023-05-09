package df.middleware.mybatis.mapping;

import df.middleware.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * @Author df
 * @Date 2022/5/26 14:57
 * @Version 1.0
 *
 * 提供连接数据库的环境，mybatis支持多环境开发，可配置多个环境，根据环境不同，使用期中一个
 * Confguration对象持有一个Environment对象
 * Environment持有一个TransactionFactory和DataSource对象，可通过解析xml配置文件的元素
 * environments来构建
*/
public class Environment {
    // 环境id，不同的id表示不同的环境
    private final String id;
    // 创建事务的工厂
    private final TransactionFactory transactionFactory;
    // 数据源
    private final DataSource dataSource;


    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }

    public static class Builder {
        private String id;
        private TransactionFactory transactionFactory;
        private DataSource dataSource;

        public Builder(String id) {
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public String id() {
            return this.id;
        }

        public Environment Builder() {
            return new Environment(this.id, this.transactionFactory, this.dataSource);
        }
    }

    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
