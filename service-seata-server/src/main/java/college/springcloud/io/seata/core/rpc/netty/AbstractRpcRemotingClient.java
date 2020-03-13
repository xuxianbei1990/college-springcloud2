package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.common.util.NetUtil;
import college.springcloud.io.seata.core.exception.FrameworkException;
import college.springcloud.io.seata.core.protocol.*;
import college.springcloud.io.seata.core.rpc.ClientMessageListener;
import college.springcloud.io.seata.core.rpc.ClientMessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 主要针对原始api的操作   发送消息， 接收消息，
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 16:46
 * Version:V1.0
 */
@Data
@Slf4j
public abstract class AbstractRpcRemotingClient extends AbstractRpcRemoting implements ClientMessageSender {

    protected final Map<Integer, MergeMessage> mergeMsgMap = new ConcurrentHashMap<>();

    private final NettyPoolKey.TransactionRole transactionRole;

    private final RpcClientBootstrap clientBootstrap;

    private NettyClientChannelManager clientChannelManager;

    private ClientMessageListener clientMessageListener;

    private ExecutorService mergeSendExecutorService;

    private static final int SCHEDULE_INTERVAL_MILLS = 5;
    private static final int MAX_MERGE_SEND_THREAD = 1;
    private static final int MAX_MERGE_SEND_MILLS = 1;
    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;

    private static final String MERGE_THREAD_PREFIX = "rpcMergeMessageSend";
    private static final String MSG_ID_PREFIX = "msgId:";
    private static final String FUTURES_PREFIX = "futures:";
    private static final String SINGLE_LOG_POSTFIX = ";";
    private static final String THREAD_PREFIX_SPLIT_CHAR = "_";
    protected final ConcurrentHashMap<String, BlockingQueue<RpcMessage>> basketMap = new ConcurrentHashMap<>();

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
        //默认true 也不知道干啥，就是一个单循环一直再执行; 用来客户端发送消息的
        if (true) {
            mergeSendExecutorService = new ThreadPoolExecutor(MAX_MERGE_SEND_THREAD,
                    MAX_MERGE_SEND_THREAD,
                    KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new NamedThreadFactory(getThreadPrefix(), MAX_MERGE_SEND_THREAD));
            mergeSendExecutorService.submit(new MergedSendRunnable());
        }
        super.init();
    }

    private String getThreadPrefix() {
        return AbstractRpcRemotingClient.MERGE_THREAD_PREFIX + THREAD_PREFIX_SPLIT_CHAR + transactionRole.name();
    }

    @Override
    public void dispatch(RpcMessage request, ChannelHandlerContext ctx) {
        if (clientMessageListener != null) {
            String remoteAddress = NetUtil.toStringAddress(ctx.channel().remoteAddress());
            clientMessageListener.onMessage(request, remoteAddress, this);
        }
    }

    /**
     * 这个其实是接收TM消息的
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof RpcMessage)) {
            return;
        }

        RpcMessage rpcMessage = (RpcMessage) msg;
        if (rpcMessage.getBody() == HeartbeatMessage.PONG) {
            if (log.isDebugEnabled()) {
                log.debug("received PONG from {}", ctx.channel().remoteAddress());
            }
            return;
        }

        //就是一个重连，非主线
        if (rpcMessage.getBody() instanceof MergeResultMessage) {
            MergeResultMessage results = (MergeResultMessage) rpcMessage.getBody();
            MergedWarpMessage mergeMessage = (MergedWarpMessage) mergeMsgMap.remove(rpcMessage.getId());
//            for (int i = 0; i < mergeMessage.msgs.size(); i++) {
//                int msgId = mergeMessage.msgIds.get(i);
//                MessageFuture future = futures.remove(msgId);
//                if (future == null) {
//                    if (log.isInfoEnabled()) {
//                        log.info("msg: {} is not found in futures.", msgId);
//                    }
//                } else {
//                    future.setResultMessage(results.getMsgs()[i]);
//                }
//            }
            return;
        }
        super.channelRead(ctx, msg);
    }

   //**如果和服务端断开连接会触发进行重连
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    /**
     * The type Merged send runnable.
     */
    private class MergedSendRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                //这个代码很有意思，对象同步，等待1 可能是降低cpu
                synchronized (mergeLock) {
                    try {
                        mergeLock.wait(MAX_MERGE_SEND_MILLS);
                    } catch (InterruptedException e) {
                    }
                }
                isSending = true;
                for (String address : basketMap.keySet()) {
                    BlockingQueue<RpcMessage> basket = basketMap.get(address);
                    if (basket.isEmpty()) {
                        continue;
                    }
                    MergedWarpMessage mergeMessage = new MergedWarpMessage();
                    while (!basket.isEmpty()) {
                        RpcMessage msg = basket.poll();
                        mergeMessage.msgs.add((AbstractMessage) msg.getBody());
                        mergeMessage.msgIds.add(msg.getId());
                    }

                    if (mergeMessage.msgIds.size() > 1) {
                        printMergeMessageLog(mergeMessage);
                    }

                    Channel sendChannel = null;
                    try {
                        sendChannel = clientChannelManager.acquireChannel(address);
                        sendRequest(sendChannel, mergeMessage);
                    } catch (FrameworkException e) {
                        log.info(e.getMessage());
                    }
                }
                isSending = false;
            }
        }

        private void printMergeMessageLog(MergedWarpMessage mergeMessage) {
            if (log.isDebugEnabled()) {
                log.debug("merge msg size:{}", mergeMessage.msgIds.size());
                for (AbstractMessage cm : mergeMessage.msgs) {
                    log.debug(cm.toString());
                }
                StringBuilder sb = new StringBuilder();
                for (long l : mergeMessage.msgIds) {
                    sb.append(MSG_ID_PREFIX).append(l).append(SINGLE_LOG_POSTFIX);
                }
                sb.append("\n");
                for (long l : futures.keySet()) {
                    sb.append(FUTURES_PREFIX).append(l).append(SINGLE_LOG_POSTFIX);
                }
                log.debug(sb.toString());
            }
        }
    }


    //客户端发送消息
    @Override
    public Object sendMsgWithResponse(Object msg) throws TimeoutException {
        return sendMsgWithResponse(msg, NettyClientConfig.getRpcRequestTimeout());
    }

    @Override
    public Object sendMsgWithResponse(Object msg, long timeout) throws TimeoutException {
        //也就是说这里实现了服务端的负载均衡
//        String validAddress = loadBalance(getTransactionServiceGroup());
        String validAddress = "localhost:8091";
        Channel channel = clientChannelManager.acquireChannel(validAddress);
        Object result = super.sendAsyncRequestWithResponse(validAddress, channel, msg, timeout);
        return result;
    }
}
