package com.df.mybatis.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.df.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author df
 * @Description: Druid 数据源工厂
 * @Date 2024/2/16 15:30
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(props.getProperty("driver"));
        dataSource.setUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        return dataSource;
    }
}
