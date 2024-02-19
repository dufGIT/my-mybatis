package com.df.mybatis.session.defaults;

import com.df.mybatis.bingding.MapperRegistry;
import com.df.mybatis.mapping.BoundSql;
import com.df.mybatis.mapping.Environment;
import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;
import com.df.mybatis.session.TransactionIsolationLevel;
import com.df.mybatis.transation.Transaction;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author df
 * @Description: 会话实现
 * @Date 2024/2/2 16:59
 */
public class DefaultSqlSession implements SqlSession {

    /**
     * 结合配置项获取信息
     */
    private Configuration configuration;
    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        try {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        // 获取环境
        Environment environment = configuration.getEnvironment();
        // 换取连接
        //Connection connection = environment.getDataSource().getConnection();

        // 获取连接另一种方式
        Transaction transaction =  environment.getTransactionFactory().newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED,false);
        Connection connection1 =  transaction.getConnection();

        // 获取sql
        BoundSql boundSql = mappedStatement.getBoundSql();
        // sql预处理语句准备
        PreparedStatement preparedStatement = connection1.prepareStatement(boundSql.getSql());
        // 执行的参数
        preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
        // 执行语句
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> objList = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
        return objList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 转换执行后的结果
    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 每次遍历行值
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    // MapperMethod里获取MappedStatement里使用
    @Override
    public Configuration getConfiguration() {
        return configuration;
    }


}
