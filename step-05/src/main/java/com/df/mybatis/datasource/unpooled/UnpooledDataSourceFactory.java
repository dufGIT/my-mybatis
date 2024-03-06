package com.df.mybatis.datasource.unpooled;

import com.df.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author df
 * @Description: 无池化数据源工厂
 * @Date 2024/2/25 16:32
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }


    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(props.getProperty("driver"));
        unpooledDataSource.setUrl(props.getProperty("url"));
        unpooledDataSource.setUsername(props.getProperty("username"));
        unpooledDataSource.setPassword(props.getProperty("password"));
        return unpooledDataSource;
    }
}
