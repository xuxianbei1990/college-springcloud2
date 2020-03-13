package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.common.thread.PositiveAtomicCounter;
import college.springcloud.io.seata.core.exception.FrameworkException;
import college.springcloud.io.seata.core.protocol.*;
import college.springcloud.io.seata.core.rpc.Disposable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 负责接收消息 发送消息  组装消息
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

    protected final PositiveAtomicCounter idGenerator = new PositiveAtomicCounter();

    protected final Object mergeLock = new Object();
    protected volatile boolean isSending = false;

    protected final ConcurrentHashMap<Integer, MessageFuture> futures = new ConcurrentHashMap<>();

    protected final Map<Integer, MergeMessage> mergeMsgMap = new ConcurrentHashMap<>();

    private final Object lock = new Object();

    private static final long NOT_WRITEABLE_CHECK_MILLS = 10L;

    protected final ConcurrentHashMap<String, BlockingQueue<RpcMessage>> basketMap = new ConcurrentHashMap<>();

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
            this.messageExecutor.execute(() -> {
                try {
                    dispatch(rpcMessage, ctx);
                } catch (Throwable th) {
                    log.error("dispatch fail {}, {}", th.getMessage(), th);
                }
            });
        }
    }


    public abstract void dispatch(RpcMessage request, ChannelHandlerContext ctx);

    protected Object sendAsyncRequestWithResponse(String address, Channel channel, Object msg, long timeout) throws
            TimeoutException {
        if (timeout <= 0) {
            throw new FrameworkException("timeout should more than 0ms");
        }
        return sendAsyncRequest(address, channel, msg, timeout);
    }

    //异步发送消息
    private Object sendAsyncRequest(String address, Channel channel, Object msg, long timeout)
            throws TimeoutException {
        if (channel == null) {
            log.warn("sendAsyncRequestWithResponse nothing, caused by null channel.");
            return null;
        }
        final RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageType(ProtocolConstants.MSGTYPE_RESQUEST_ONEWAY);
        rpcMessage.setCodec(ProtocolConstants.CONFIGURED_CODEC);
        rpcMessage.setCompressor(ProtocolConstants.CONFIGURED_COMPRESSOR);
        rpcMessage.setBody(msg);

        final MessageFuture messageFuture = new MessageFuture();
        messageFuture.setRequestMessage(rpcMessage);
        messageFuture.setTimeout(timeout);
        futures.put(rpcMessage.getId(), messageFuture);

        if (address != null) {
            /*
            The batch send.
            Object From big to small: RpcMessage -> MergedWarpMessage -> AbstractMessage
            @see AbstractRpcRemotingClient.MergedSendRunnable
            */
            if (NettyClientConfig.isEnableClientBatchSendRequest()) {
                ConcurrentHashMap<String, BlockingQueue<RpcMessage>> map = basketMap;
                BlockingQueue<RpcMessage> basket = map.get(address);
                if (basket == null) {
                    map.putIfAbsent(address, new LinkedBlockingQueue<>());
                    basket = map.get(address);
                }
                basket.offer(rpcMessage);
                if (log.isDebugEnabled()) {
                    log.debug("offer message: {}", rpcMessage.getBody());
                }
                //第一为什么用这种方式，第二为什么发送的线程不放这里？
                if (!isSending) {
                    synchronized (mergeLock) {
                        mergeLock.notifyAll();
                    }
                }
            } else {
                // the single send.
//                sendSingleRequest(channel, msg, rpcMessage);
                if (log.isDebugEnabled()) {
                    log.debug("send this msg[{}] by single send.", msg);
                }
            }
        } else {
//            sendSingleRequest(channel, msg, rpcMessage);
        }
        return null;
    }

    //最终发送消息
    protected void sendRequest(Channel channel, Object msg) {
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setMessageType(msg instanceof HeartbeatMessage ?
                ProtocolConstants.MSGTYPE_HEARTBEAT_REQUEST
                : ProtocolConstants.MSGTYPE_RESQUEST);
        rpcMessage.setCodec(ProtocolConstants.CONFIGURED_CODEC);
        rpcMessage.setCompressor(ProtocolConstants.CONFIGURED_COMPRESSOR);
        rpcMessage.setBody(msg);
        rpcMessage.setId(getNextMessageId());
        if (msg instanceof MergeMessage) {
            mergeMsgMap.put(rpcMessage.getId(), (MergeMessage) msg);
        }
        //检查通道是否可以写。这个才是重点。直接体现对netty的熟悉
//        channelWritableCheck(channel, msg);
        if (log.isDebugEnabled()) {
            log.debug("write message:" + rpcMessage.getBody() + ", channel:" + channel + ",active?"
                    + channel.isActive() + ",writable?" + channel.isWritable() + ",isopen?" + channel.isOpen());
        }
        channel.writeAndFlush(rpcMessage);
    }

    public int getNextMessageId() {
        return idGenerator.incrementAndGet();
    }

    private void channelWritableCheck(Channel channel, Object msg) {
        int tryTimes = 0;
        synchronized (lock) {
            while (!channel.isWritable()) {
                try {
                    tryTimes++;
                    if (tryTimes > NettyClientConfig.getMaxNotWriteableRetry()) {
//                        destroyChannel(channel);
                        throw new FrameworkException("msg:" + ((msg == null) ? "null" : msg.toString()));
                    }
                    lock.wait(NOT_WRITEABLE_CHECK_MILLS);
                } catch (InterruptedException exx) {
                    log.error(exx.getMessage());
                }
            }
        }
    }
}
