package df.middleware.mybatis.datasource.pooled;

import df.middleware.mybatis.datasource.unpooled.UnpooledDataSource;
import df.middleware.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * @Author df
 * @Date 2022/6/2 9:17
 * @Version 1.0
 * 继承无池化factory，主要目的减少一些基础重复的配置，如设置获取数据源信息
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }
}
