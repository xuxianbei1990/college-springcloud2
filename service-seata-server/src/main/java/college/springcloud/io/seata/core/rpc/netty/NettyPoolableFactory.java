package college.springcloud.io.seata.core.rpc.netty;

import io.netty.channel.Channel;
import org.apache.commons.pool.KeyedPoolableObjectFactory;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 9:41
 * Version:V1.0
 */
public class NettyPoolableFactory implements KeyedPoolableObjectFactory<NettyPoolKey, Channel> {

    private final AbstractRpcRemotingClient rpcRemotingClient;

    private final RpcClientBootstrap clientBootstrap;

    public NettyPoolableFactory(AbstractRpcRemotingClient rpcRemotingClient, RpcClientBootstrap clientBootstrap) {
        this.rpcRemotingClient = rpcRemotingClient;
        this.clientBootstrap = clientBootstrap;
    }

    @Override
    public Channel makeObject(NettyPoolKey key) throws Exception {
        return null;
    }

    @Override
    public void destroyObject(NettyPoolKey key, Channel obj) throws Exception {

    }

    @Override
    public boolean validateObject(NettyPoolKey key, Channel obj) {
        return false;
    }

    @Override
    public void activateObject(NettyPoolKey key, Channel obj) throws Exception {

    }

    @Override
    public void passivateObject(NettyPoolKey key, Channel obj) throws Exception {

    }
}
