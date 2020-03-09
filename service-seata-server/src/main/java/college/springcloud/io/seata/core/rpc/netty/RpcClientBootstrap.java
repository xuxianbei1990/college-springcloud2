package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.core.rpc.netty.v1.ProtocolV1Decoder;
import college.springcloud.io.seata.core.rpc.netty.v1.ProtocolV1Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 16:51
 * Version:V1.0
 * 这里我为什么不用 RemotingClient 而是直接用RemotingService
 * 因为，我不会扩展RemotingClient.
 */
@Slf4j
public class RpcClientBootstrap implements RemotingService {

    private final NettyClientConfig nettyClientConfig;
    private final NettyPoolKey.TransactionRole transactionRole;
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroupWorker;
    private static final String THREAD_PREFIX_SPLIT_CHAR = "_";
    private EventExecutorGroup defaultEventExecutorGroup;
    private final ChannelHandler channelHandler;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public RpcClientBootstrap(NettyClientConfig nettyClientConfig, final EventExecutorGroup eventExecutorGroup,
                              ChannelHandler channelHandler, NettyPoolKey.TransactionRole transactionRole) {
        this.nettyClientConfig = nettyClientConfig;
        int selectorThreadSize = this.nettyClientConfig.getClientSelectorThreadSize();
        this.transactionRole = transactionRole;
        this.eventLoopGroupWorker = new NioEventLoopGroup(selectorThreadSize,
                new NamedThreadFactory(getThreadPrefix(this.nettyClientConfig.getClientSelectorThreadPrefix()),
                        selectorThreadSize));
        this.defaultEventExecutorGroup = eventExecutorGroup;
        this.channelHandler = channelHandler;
    }

    private String getThreadPrefix(String threadPrefix) {
        return threadPrefix + THREAD_PREFIX_SPLIT_CHAR + transactionRole.name();
    }

    @Override
    public void start() {
        if (this.defaultEventExecutorGroup == null) {
            this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(nettyClientConfig.getClientWorkerThreads(),
                    new NamedThreadFactory(getThreadPrefix(nettyClientConfig.getClientWorkerThreadPrefix()),
                            nettyClientConfig.getClientWorkerThreads()));
        }
        this.bootstrap.group(this.eventLoopGroupWorker).channel(
                nettyClientConfig.getClientChannelClazz())
                //不延迟, 允许小包发送
                .option(ChannelOption.TCP_NODELAY, true)
                //保持长连接
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())
                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())
                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize());

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(
                        new IdleStateHandler(nettyClientConfig.getChannelMaxReadIdleSeconds(),
                                nettyClientConfig.getChannelMaxWriteIdleSeconds(),
                                nettyClientConfig.getChannelMaxAllIdleSeconds()))
                        .addLast(new ProtocolV1Decoder())
                        .addLast(new ProtocolV1Encoder());
                if (null != channelHandler) {
                    ch.pipeline().addLast(channelHandler);
                }
            }
        });
        if (initialized.compareAndSet(false, true)) {
            log.info("RpcClientBootstrap has started");
        }
        connectServer();
    }

    private void connectServer() {
        //seata不是这么干的，seata是正常的连接
        try {
            ChannelFuture channelFuture =
                    bootstrap.connect((new InetSocketAddress("localhost", 8091))).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                eventLoopGroupWorker.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {

    }
}
