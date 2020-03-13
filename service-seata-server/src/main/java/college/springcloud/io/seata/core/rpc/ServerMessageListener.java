package college.springcloud.io.seata.core.rpc;

import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.netty.RegisterCheckAuthHandler;
import io.netty.channel.ChannelHandlerContext;

/**  消息接收
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 16:57
 * Version:V1.0
 */
public interface ServerMessageListener {

    //这里不单单把RM注册进来，还把通道各种绑定。德玛西亚
    void onRegRmMessage(RpcMessage request, ChannelHandlerContext ctx,
                        ServerMessageSender sender, RegisterCheckAuthHandler checkAuthHandler);


    void onTrxMessage(RpcMessage request, ChannelHandlerContext ctx, ServerMessageSender sender);
}
