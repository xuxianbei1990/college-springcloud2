package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.core.rpc.RpcContext;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:13
 * Version:V1.0
 */
public class ChannelManager {
    private static final ConcurrentMap<Channel, RpcContext> IDENTIFIED_CHANNELS
            = new ConcurrentHashMap<Channel, RpcContext>();

    public static boolean isRegistered(Channel channel) {
        return IDENTIFIED_CHANNELS.containsKey(channel);
    }

    public static RpcContext getContextFromIdentified(Channel channel) {
        return IDENTIFIED_CHANNELS.get(channel);
    }
}
