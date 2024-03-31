package com.df.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author df
 * @Description: 数据源工厂
 * @Date 2024/2/16 15:28
 */
public interface DataSourceFactory {
    void setProperties(Properties props);

    DataSource getDataSource();

}
