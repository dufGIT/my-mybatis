package com.df.mybatis.datasource.pooled;

import com.df.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @Author df
 * @Description: 有连接池的数据源工厂
 * 此处继承UnpooledDataSourceFactory，对于取属性都是重复的工作而且都会用到所以此处继承最好
 * @Date 2024/2/26 16:21
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(props.getProperty("driver"));
        pooledDataSource.setUrl(props.getProperty("url"));
        pooledDataSource.setUsername(props.getProperty("username"));
        pooledDataSource.setPassword(props.getProperty("password"));
        return pooledDataSource;
    }
}
