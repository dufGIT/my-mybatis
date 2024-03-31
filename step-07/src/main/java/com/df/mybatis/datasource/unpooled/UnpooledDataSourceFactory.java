package com.df.mybatis.datasource.unpooled;

import com.df.mybatis.datasource.DataSourceFactory;
import com.df.mybatis.reflection.MetaObject;
import com.df.mybatis.reflection.SystemMetaObject;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author df
 * @Description: 无池化数据源工厂
 * @Date 2024/2/25 16:32
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected DataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Properties props) {
        // 将当前类传进去进行解析
        MetaObject metaObject = SystemMetaObject.forObject(dataSource);
        for (Object key : props.keySet()) {
            String propertyName = (String) key;
            // 当前字段是否有set方法
            if (metaObject.hasSetter(propertyName)) {
                String value = props.getProperty(propertyName);
                Object convertedValue = convertValue(metaObject, propertyName, value);
                // 反射插入到dataSource某个属性的值。
                metaObject.setValue(propertyName, convertedValue);
            }
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 根据setter的类型,将配置文件中的值强转成相应的类型
     */
    private Object convertValue(MetaObject metaObject, String propertyName, String value) {
        Object convertedValue = value;
        // 查询当前属性的set方法的类型值。
        Class<?> targetType = metaObject.getSetterType(propertyName);
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            convertedValue = Long.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }

}
