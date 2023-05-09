package df.middleware.mybatis.session.defaults;

import df.middleware.mybatis.executor.Executor;
import df.middleware.mybatis.mapping.BoundSql;
import df.middleware.mybatis.mapping.Environment;
import df.middleware.mybatis.mapping.MappedStatement;
import df.middleware.mybatis.session.Configuration;
import df.middleware.mybatis.session.RowBounds;
import df.middleware.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    //private MapperRegistry mapperRegistry;
    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public int update(String statement, Object parameter) {
        // 查询要处理哪个mapper语句
        MappedStatement ms = configuration.getMappedStatement(statement);
        try {
            return executor.update(ms, parameter);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating database.  Cause: " + e);
        }
    }

    @Override
    public int insert(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public Object delete(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = this.<T>selectList(statement, parameter);
        try {
            if (list.size() == 0) {
                return list.get(0);
            } else if (list.size() > 1) {
                throw new RuntimeException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        System.out.println("执行查询 statement：" + statement + "parameter：" + parameter);
        MappedStatement ms = configuration.getMappedStatement(statement);
        try {
            return executor.query(ms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
        } catch (SQLException e) {
            throw new RuntimeException("Error querying database.  Cause: " + e);
        }
    }


    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void commit() {
        try {
            executor.commit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Error committing transaction.  Cause: " + e);
        }
    }
}
