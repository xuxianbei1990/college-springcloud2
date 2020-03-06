package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.core.rpc.netty.v1.ProtocolV1Decoder;
import college.springcloud.io.seata.core.rpc.netty.v1.ProtocolV1Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:35
 * Version:V1.0
 */
@Slf4j
@Data
public abstract class AbstractRpcRemotingServer extends AbstractRpcRemoting implements RemotingService {

    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupWorker;
    private final EventLoopGroup eventLoopGroupBoss;
    private final NettyServerConfig nettyServerConfig;
    private int listenPort;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * The Channel handlers.
     */
    protected ChannelHandler[] channelHandlers;


    protected void setChannelHandlers(ChannelHandler... handlers) {
        this.channelHandlers = handlers;
    }

    public AbstractRpcRemotingServer(final NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
        this.serverBootstrap = new ServerBootstrap();
        //netty epoll 模式。这个可以放下
        eventLoopGroupBoss = new NioEventLoopGroup(nettyServerConfig.getBossThreadSize());
        eventLoopGroupWorker = new NioEventLoopGroup(nettyServerConfig.getServerWorkerThreads(),
                new NamedThreadFactory(nettyServerConfig.getWorkerThreadPrefix(),
                        nettyServerConfig.getServerWorkerThreads()));
        setListenPort(nettyServerConfig.getDefaultListenPort());
    }

    /**
     * Sets listen port.
     *
     * @param listenPort the listen port
     */
    public void setListenPort(int listenPort) {

        if (listenPort <= 0) {
            throw new IllegalArgumentException("listen port: " + listenPort + " is invalid!");
        }
        this.listenPort = listenPort;
    }

    /**
     * 下面是：建立服务端各种细节配置。
     */
    @Override
    public void start() {
        this.serverBootstrap.group(this.eventLoopGroupBoss, this.eventLoopGroupWorker)
                .channel(NioServerSocketChannel.class)
                //设置服务端连接等待队列大小。
                .option(ChannelOption.SO_BACKLOG, 1024)
                //一般来说，一个端口释放后会等待两分钟之后才能再被使用，SO_REUSEADDR是让端口释放后立即就可以被再次使用
                //SO_REUSEADDR用于对TCP套接字处于TIME_WAIT状态下的socket，才可以重复绑定使用
                .option(ChannelOption.SO_REUSEADDR, true)
                //保持长链接，2小时内无数据，会探路一次
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //启动TCP_NODELAY，就意味着禁用了Nagle算法，允许小包的发送。
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 153600)
                .childOption(ChannelOption.SO_RCVBUF, 153600)
//                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
//                        new WriteBufferWaterMark(nettyServerConfig.getWriteBufferLowWaterMark(),
//                                nettyServerConfig.getWriteBufferHighWaterMark()))
                .localAddress(new InetSocketAddress(listenPort))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        //心跳机制？我记得以前一般是定义协议的规则
                        channel.pipeline().addLast(new IdleStateHandler(15, 0, 0))
                                .addLast(new ProtocolV1Decoder())
                                .addLast(new ProtocolV1Encoder());
                        if (null != channelHandlers) {
                            addChannelPipelineLast(channel, channelHandlers);
                        }
                    }
                });
        try {
            ChannelFuture future = this.serverBootstrap.bind(listenPort).sync();
            log.info("Server started ...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
