package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.ServerMessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:33
 * Version:V1.0
 */
@ChannelHandler.Sharable
public class RpcServer extends AbstractRpcRemotingServer implements ServerMessageSender {

    private int listenPort;

    public RpcServer(NettyServerConfig nettyServerConfig) {
        super(nettyServerConfig);
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


    @Override
    public void init() {
        super.init();
        setChannelHandlers(RpcServer.this);
        super.start();
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
