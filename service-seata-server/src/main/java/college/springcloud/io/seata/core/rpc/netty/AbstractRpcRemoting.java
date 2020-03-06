package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.common.thread.NamedThreadFactory;
import college.springcloud.io.seata.core.rpc.Disposable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**  负责接收消息 发送消息
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:37
 * Version:V1.0
 */
public abstract class AbstractRpcRemoting extends ChannelDuplexHandler implements Disposable {

    private static final int TIMEOUT_CHECK_INTERNAL = 3000;

    /**
     * The Timer executor.
     */
    protected final ScheduledExecutorService timerExecutor = new ScheduledThreadPoolExecutor(1,
            new NamedThreadFactory("timeoutChecker", 1, true));

    public void init() {
        //反正一个定时任务，按照固定频率扫描
        timerExecutor.scheduleAtFixedRate(() ->{
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
        }, TIMEOUT_CHECK_INTERNAL, TIMEOUT_CHECK_INTERNAL, TimeUnit.MILLISECONDS);
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
}
