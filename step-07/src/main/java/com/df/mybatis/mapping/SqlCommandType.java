package com.df.mybatis.mapping;

/**
 * @Author df
 * @Description: SQL 指令类型
 * @Date 2024/2/7 13:01
 */
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;

}
