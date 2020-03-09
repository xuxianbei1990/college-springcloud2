package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.common.thread.RejectedPolicies;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 16:03
 * Version:V1.0
 */
@ChannelHandler.Sharable
public class TmRpcClient extends AbstractRpcRemotingClient {
    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;
    private static final int MAX_QUEUE_SIZE = 2000;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private static volatile TmRpcClient instance;

    public static boolean enableDegrade = false;

    public static TmRpcClient getInstance(String applicationId, String transactionServiceGroup) {
        TmRpcClient tmRpcClient = getInstance();
        return tmRpcClient;
    }

    public static TmRpcClient getInstance() {
        if (null == instance) {
            synchronized (TmRpcClient.class) {
                if (null == instance) {
                    NettyClientConfig nettyClientConfig = new NettyClientConfig();
                    final ThreadPoolExecutor messageExecutor = new ThreadPoolExecutor(
                            nettyClientConfig.getClientWorkerThreads(), nettyClientConfig.getClientWorkerThreads(),
                            KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
                            new NamedThreadFactory(nettyClientConfig.getTmDispatchThreadPrefix(),
                                    nettyClientConfig.getClientWorkerThreads()),
                            RejectedPolicies.runsOldestTaskPolicy());
                    instance = new TmRpcClient(nettyClientConfig, null, messageExecutor);
                }
            }
        }
        return instance;
    }

    public TmRpcClient(NettyClientConfig nettyClientConfig,
                       EventExecutorGroup eventExecutorGroup,
                       ThreadPoolExecutor messageExecutor) {
        super(nettyClientConfig, eventExecutorGroup, messageExecutor, NettyPoolKey.TransactionRole.TMROLE);
    }

    @Override
    public void init() {
        if (initialized.compareAndSet(false, true)) {
            enableDegrade = false;
            super.init();
        }
    }

    @Override
    protected Function<String, NettyPoolKey> getPoolKeyFunction() {
        return null;
    }

    @Override
    protected String getTransactionServiceGroup() {
        return null;
    }
}
