package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.core.protocol.RegisterRMRequest;
import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.DefaultServerMessageListenerImpl;
import college.springcloud.io.seata.core.rpc.ServerMessageListener;
import college.springcloud.io.seata.core.rpc.ServerMessageSender;
import college.springcloud.io.seata.core.rpc.TransactionMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:33
 * Version:V1.0
 */
@Data
@ChannelHandler.Sharable
public class RpcServer extends AbstractRpcRemotingServer implements ServerMessageSender {


    protected ServerMessageListener serverMessageListener;

    private TransactionMessageHandler transactionMessageHandler;

    public RpcServer(NettyServerConfig nettyServerConfig) {
        super(nettyServerConfig, null);
    }

    public void setHandler(TransactionMessageHandler transactionMessageHandler) {
        this.transactionMessageHandler = transactionMessageHandler;
    }


    @Override
    public void init() {
        super.init();
        setChannelHandlers(RpcServer.this);
        DefaultServerMessageListenerImpl defaultServerMessageListenerImpl = new DefaultServerMessageListenerImpl(
                //这个实例就是 DefaultCoordinator
                transactionMessageHandler);
        super.start();
    }

    @Override
    public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
        Object msg = request.getBody();
        /**
         * 把各个RM注册过来
         */
        if (msg instanceof RegisterRMRequest) {
            serverMessageListener.onRegRmMessage(request, ctx, this, null);
        } else {
            //说明初始化时候，已经把通道注册进来了
            if (ChannelManager.isRegistered(ctx.channel())) {
                serverMessageListener.onTrxMessage(request, ctx, this);
            }
        }
    }

    @Override
    public void sendResponse(RpcMessage request, Channel channel, Object msg) {

    }

    @Override
    public Object sendSyncRequest(String resourceId, String clientId, Object message, long timeout) throws IOException, TimeoutException {
        return null;
    }

    @Override
    public Object sendSyncRequest(String resourceId, String clientId, Object message) throws IOException, TimeoutException {
        return null;
    }

    @Override
    public Object sendSyncRequest(Channel clientChannel, Object message) throws TimeoutException {
        return null;
    }

    @Override
    public Object sendSyncRequest(Channel clientChannel, Object message, long timeout) throws TimeoutException {
        return null;
    }

    @Override
    public Object sendASyncRequest(Channel channel, Object message) throws IOException, TimeoutException {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
