package college.springcloud.io.seata.core.rpc;

import college.springcloud.io.seata.common.util.NetUtil;
import college.springcloud.io.seata.core.protocol.*;
import college.springcloud.io.seata.core.rpc.netty.ChannelManager;
import college.springcloud.io.seata.core.rpc.netty.RegisterCheckAuthHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:00
 * Version:V1.0
 */
@Slf4j
public class DefaultServerMessageListenerImpl implements ServerMessageListener {

    //这个实例就是 DefaultCoordinator
    private final TransactionMessageHandler transactionMessageHandler;

    public DefaultServerMessageListenerImpl(TransactionMessageHandler transactionMessageHandler) {
        this.transactionMessageHandler = transactionMessageHandler;
    }

    @Override
    public void onRegRmMessage(RpcMessage request, ChannelHandlerContext ctx, ServerMessageSender sender, RegisterCheckAuthHandler checkAuthHandler) {

    }

    @Override
    public void onTrxMessage(RpcMessage request, ChannelHandlerContext ctx, ServerMessageSender sender) {
        Object message = request.getBody();
        RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
        log.info(NetUtil.toIpAddress(ctx.channel().remoteAddress()), rpcContext.getTransactionServiceGroup());

        if (!(message instanceof AbstractMessage)) {
            return;
        }

        /**
         * 事务开始时候，服务端：存储全局事务到表里，然后统计了下  返回一个XID。 管他是啥，反正是一个唯一id
         * 这个XID 是客户端生成了？
         */
        if (message instanceof MergedWarpMessage) {
            AbstractResultMessage[] results = new AbstractResultMessage[((MergedWarpMessage) message).msgs.size()];
            for (int i = 0; i < results.length; i++) {
                final AbstractMessage subMessage = ((MergedWarpMessage) message).msgs.get(i);
                results[i] = transactionMessageHandler.onRequest(subMessage, rpcContext);
            }

            MergeResultMessage resultMessage = new MergeResultMessage();
            resultMessage.setMsgs(results);
            sender.sendResponse(request, ctx.channel(), resultMessage);
        }
    }
}
