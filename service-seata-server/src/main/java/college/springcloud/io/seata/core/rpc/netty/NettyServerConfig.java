package college.springcloud.io.seata.core.rpc.netty;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:40
 * Version:V1.0
 */
@Data
public class NettyServerConfig {

    private static int WORKER_THREAD_SIZE = NettyRuntime.availableProcessors() * 2;
    private int serverSelectorThreads = WORKER_THREAD_SIZE;
    private int serverSocketSendBufSize = 153600;
    private int serverSocketResvBufSize = 153600;
    private int serverWorkerThreads = WORKER_THREAD_SIZE;
    private int soBackLogSize = 1024;
    private int writeBufferHighWaterMark = 67108864;
    private int writeBufferLowWaterMark = 1048576;
    private static final int DEFAULT_LISTEN_PORT = 8091;
    private static final int RPC_REQUEST_TIMEOUT = 30 * 1000;
    private boolean enableServerPooledByteBufAllocator = true;
    private int serverChannelMaxIdleTimeSeconds = 30;
    private static final String DEFAULT_BOSS_THREAD_PREFIX = "NettyBoss";
    private static final String EPOLL_WORKER_THREAD_PREFIX = "NettyServerEPollWorker";
    private static final String NIO_WORKER_THREAD_PREFIX = "NettyServerNIOWorker";
    private static final String DEFAULT_EXECUTOR_THREAD_PREFIX = "NettyServerBizHandler";
    private static final int DEFAULT_BOSS_THREAD_SIZE = 1;

    /**
     * Shutdown timeout default 1s
     */
    private static final int DEFAULT_SHUTDOWN_TIMEOUT_SEC = 3;

    /**
     * The Server channel clazz.
     */
    public static final Class<? extends ServerChannel> SERVER_CHANNEL_CLAZZ = NioServerSocketChannel.class;

    /**
     * The constant DIRECT_BYTE_BUF_ALLOCATOR.
     */
    public static final PooledByteBufAllocator DIRECT_BYTE_BUF_ALLOCATOR =
            new PooledByteBufAllocator(
                    true,
                    WORKER_THREAD_SIZE,
                    WORKER_THREAD_SIZE,
                    2048 * 64,
                    10,
                    512,
                    256,
                    64,
                    true,
                    0
            );

    public int getBossThreadSize() {
        return DEFAULT_BOSS_THREAD_SIZE;
    }

    public String getWorkerThreadPrefix() {
        return NIO_WORKER_THREAD_PREFIX;
    }

    public int getDefaultListenPort() {
        return DEFAULT_LISTEN_PORT;
    }
}
