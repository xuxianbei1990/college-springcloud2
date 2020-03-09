package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.util.NetUtil;
import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.ClientMessageListener;
import college.springcloud.io.seata.core.rpc.ClientMessageSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 16:46
 * Version:V1.0
 */
@Data
public abstract class AbstractRpcRemotingClient extends AbstractRpcRemoting implements ClientMessageSender {

    private final NettyPoolKey.TransactionRole transactionRole;

    private final RpcClientBootstrap clientBootstrap;

    private NettyClientChannelManager clientChannelManager;

    private ClientMessageListener clientMessageListener;

    private static final int SCHEDULE_INTERVAL_MILLS = 5;

    public AbstractRpcRemotingClient(NettyClientConfig nettyClientConfig,
                                     EventExecutorGroup eventExecutorGroup,
                                     ThreadPoolExecutor messageExecutor,
                                     NettyPoolKey.TransactionRole transactionRole) {
        super(messageExecutor);
        this.transactionRole = transactionRole;
        this.clientBootstrap = new RpcClientBootstrap(nettyClientConfig, eventExecutorGroup, this, transactionRole);
        this.clientChannelManager = new NettyClientChannelManager(
                new NettyPoolableFactory(this, clientBootstrap), getPoolKeyFunction(), nettyClientConfig);
    }


    protected abstract Function<String, NettyPoolKey> getPoolKeyFunction();

    protected abstract String getTransactionServiceGroup();

    @Override
    public void init() {
        clientBootstrap.start();
        //就是一个重新链接 在seata 是这里发起链接的
//        timerExecutor.scheduleAtFixedRate(() ->
//                        clientChannelManager.reconnect(getTransactionServiceGroup()),
//                SCHEDULE_INTERVAL_MILLS, SCHEDULE_INTERVAL_MILLS, TimeUnit.SECONDS);
        super.init();
    }

    @Override
    public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
        if (clientMessageListener != null) {
            String remoteAddress = NetUtil.toStringAddress(ctx.channel().remoteAddress());
            clientMessageListener.onMessage(request, remoteAddress, this);
        }
    }
}
