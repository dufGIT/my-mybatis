package com.df.mybatis.bingding;

import com.df.mybatis.mapping.MappedStatement;
import com.df.mybatis.mapping.SqlCommandType;
import com.df.mybatis.session.Configuration;
import com.df.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * @Author df
 * @Description: 映射器方法
 * @Date 2024/2/7 17:33
 */
public class MapperMethod {
    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    // 执行器
    public Object execute(SqlSession sqlSession,Object[] args){
        Object result=null;
        switch (command.getType()){
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                // 暂时实现select
                result=sqlSession.selectOne(command.getName(),args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    public static class SqlCommand{
        // id
        private final String name;
        // 执行sql的类型
        private final SqlCommandType type;

        // 获取当前执行语句的namespace和id以及sql类型
        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName=mapperInterface.getName()+"."+method.getName();
            MappedStatement ms=configuration.getMappedStatement(statementName);
            this.name = ms.getId();
            this.type = ms.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }

    }
}
