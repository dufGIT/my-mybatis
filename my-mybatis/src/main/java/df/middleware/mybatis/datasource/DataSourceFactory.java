package df.middleware.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author df
 * @Date 2022/5/26 14:45
 * @Version 1.0
 * 数据源工厂
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
}
