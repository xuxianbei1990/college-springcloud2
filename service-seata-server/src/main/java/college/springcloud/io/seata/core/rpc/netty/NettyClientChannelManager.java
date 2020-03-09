package college.springcloud.io.seata.core.rpc.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 9:38
 * Version:V1.0
 */
@Slf4j
public class NettyClientChannelManager {

    //反正是一个对象连接池。
    private final GenericKeyedObjectPool<NettyPoolKey, Channel> nettyClientKeyPool;

    private Function<String, NettyPoolKey> poolKeyFunction;

    //本地内存加锁。我比较好奇为什么这么写？
    private final ConcurrentMap<String, Object> channelLocks = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, NettyPoolKey> poolKeyMap = new ConcurrentHashMap<>();

    NettyClientChannelManager(final NettyPoolableFactory keyPoolableFactory, final Function<String, NettyPoolKey> poolKeyFunction,
                              final NettyClientConfig clientConfig) {
        nettyClientKeyPool = new GenericKeyedObjectPool(keyPoolableFactory);
        nettyClientKeyPool.setConfig(getNettyPoolConfig(clientConfig));
        this.poolKeyFunction = poolKeyFunction;
    }

    private GenericKeyedObjectPool.Config getNettyPoolConfig(final NettyClientConfig clientConfig) {
        GenericKeyedObjectPool.Config poolConfig = new GenericKeyedObjectPool.Config();
        poolConfig.maxActive = clientConfig.getMaxPoolActive();
        poolConfig.minIdle = clientConfig.getMinPoolIdle();
        poolConfig.maxWait = clientConfig.getMaxAcquireConnMills();
        poolConfig.testOnBorrow = clientConfig.isPoolTestBorrow();
        poolConfig.testOnReturn = clientConfig.isPoolTestReturn();
        poolConfig.lifo = clientConfig.isPoolLifo();
        return poolConfig;
    }

    void reconnect(String transactionServiceGroup) {
        try {
            InetSocketAddress serverAddress = new InetSocketAddress(8091);

            acquireChannel(serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort());
        } catch (Exception e) {
            log.error("{} can not connect to {} cause:{}", e.getMessage(), e);
        }

    }

    Channel acquireChannel(String serverAddress) {
        channelLocks.putIfAbsent(serverAddress, new Object());
        synchronized (channelLocks.get(serverAddress)) {
            return doConnect(serverAddress);
        }
    }

    private Channel doConnect(String serverAddress) {
        Channel channelFromPool;
        try {
            NettyPoolKey currentPoolKey = poolKeyFunction.apply(serverAddress);
            NettyPoolKey previousPoolKey = poolKeyMap.putIfAbsent(serverAddress, currentPoolKey);
            channelFromPool = nettyClientKeyPool.borrowObject(poolKeyMap.get(serverAddress));
        } catch (Exception e) {
            throw new  RuntimeException("ss");
        }
        return channelFromPool;
    }
}
