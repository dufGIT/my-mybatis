package com.df.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Description: 池状态
 * 用于记录连接池运行时的状态，比如连接获取次数，无效连接数量等。
 * 同时 PoolState 内部定义了两个 PooledConnection 集合，用于存储空闲连接和活跃连接
 * @Date 2024/2/25 20:48
 */
public class PoolState {
    protected PooledDataSource dataSource;

    // 空闲链接列表
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    // 活跃链接列表
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    // 从连接池中获取连接的次数
    protected long requestCount = 0;
    // 请求连接总耗时（单位：毫秒）
    protected long accumulatedRequestTime = 0;
    // 连接执行时间总耗时
    protected long accumulatedCheckoutTime = 0;
    // 执行时间超时的连接数
    protected long claimedOverdueConnectionCount = 0;
    // 超时时间累加值
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;
    // 等待时间累加值
    protected long accumulatedWaitTime = 0;
    // 等待次数
    protected long hadToWaitCount = 0;
    // 无效连接数
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    // 执行平均耗时时间
    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    // 平均等待时间
    public synchronized long getAverageWaitTime() {
        return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
    }

    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    // 平均执行超时耗时
    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    // 平执行耗时
    public synchronized long getAverageCheckoutTime() {
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    // 空闲连接池子数
    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    // 活跃连接池子数
    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }


}
