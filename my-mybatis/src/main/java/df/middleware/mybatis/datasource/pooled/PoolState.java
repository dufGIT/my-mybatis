package df.middleware.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author df
 * @Date 2022/6/1 11:04
 * @Version 1.0
 * 简单理解可以认为PoolState是连接池
 */
public class PoolState {

    // 空闲链表
    protected final List<PooledConnection> idelConnections = new ArrayList<>();
    // 活跃链接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    // 请求次数
    protected long requestCount = 0;
    // 总请求时间
    protected long accumulatedRequestTime = 0;
    protected long accumulatedCheckoutTime = 0;
    // 过期连接的数量
    protected long claimedOverdueConnectionCount = 0;
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    // 总等待时间
    protected long accumulatedWaitTime=0;
    // 要等待的次数
    protected long hadToWaitCount=0;
    // 失败连接的次数
    protected long badConnectionCount=0;


    protected PooledDataSource dataSource;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
