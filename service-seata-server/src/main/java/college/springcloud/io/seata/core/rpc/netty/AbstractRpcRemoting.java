package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.core.protocol.ProtocolConstants;
import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.Disposable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 负责接收消息 发送消息
 *
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:37
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractRpcRemoting extends ChannelDuplexHandler implements Disposable {

    private static final int TIMEOUT_CHECK_INTERNAL = 3000;

    //用于消息接收线程池
    protected final ThreadPoolExecutor messageExecutor;

    public AbstractRpcRemoting(ThreadPoolExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }


    /**
     * The Timer executor.
     */
    protected final ScheduledExecutorService timerExecutor = new ScheduledThreadPoolExecutor(1,
            new NamedThreadFactory("timeoutChecker", 1, true));

    public void init() {
        //反正一个定时任务，按照固定频率扫描
//        timerExecutor.scheduleAtFixedRate(() ->{
//            for (Map.Entry<Integer, MessageFuture> entry : futures.entrySet()) {
//                if (entry.getValue().isTimeout()) {
//                    futures.remove(entry.getKey());
//                    entry.getValue().setResultMessage(null);
//                    if (LOGGER.isDebugEnabled()) {
//                        LOGGER.debug("timeout clear future: {}", entry.getValue().getRequestMessage().getBody());
//                    }
//                }
//            }
//
//            nowMills = System.currentTimeMillis();
//        }, TIMEOUT_CHECK_INTERNAL, TIMEOUT_CHECK_INTERNAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Add channel pipeline last.
     *
     * @param channel  the channel
     * @param handlers the handlers
     */
    protected void addChannelPipelineLast(Channel channel, ChannelHandler... handlers) {
        if (null != channel && null != handlers) {
            channel.pipeline().addLast(handlers);
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof RpcMessage)) {
            return;
        }
        final RpcMessage rpcMessage = (RpcMessage) msg;
        //如果是request或者request 不需要响应的
        if ((rpcMessage.getMessageType() == ProtocolConstants.MSGTYPE_RESQUEST
                || rpcMessage.getMessageType() == ProtocolConstants.MSGTYPE_RESQUEST_ONEWAY)) {
            //我比较好奇这里为什么另外启动线程去执行？
            this.messageExecutor.execute(()->{
                try {
                    dispatch(rpcMessage, ctx);
                } catch (Throwable th) {
                    log.error("dispatch fail {}, {}", th.getMessage(), th);
                }
            });
        }
    }


    public abstract void dispatch(RpcMessage request, ChannelHandlerContext ctx);
}
