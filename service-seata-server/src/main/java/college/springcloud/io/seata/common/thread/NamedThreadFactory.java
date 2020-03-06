package college.springcloud.io.seata.common.thread;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xuxianbei
 * Date: 2020/3/2
 * Time: 15:16
 * Version:V1.0
 */
public class NamedThreadFactory implements ThreadFactory {

    //todo 为什么用atomicInteger
    private final static Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();
    private final String prefix;
    private final int totalSize;
    //是否是守护线程
    private final boolean makeDaemons;

    public NamedThreadFactory(String prefix, int totalSize, boolean makeDaemons) {
        PREFIX_COUNTER.putIfAbsent(prefix, new AtomicInteger(0));
        int prefixCounter = PREFIX_COUNTER.get(prefix).incrementAndGet();
        this.prefix = prefix + "_" + prefixCounter;
        this.makeDaemons = makeDaemons;
        this.totalSize = totalSize;
    }

    /**
     * Instantiates a new Named thread factory.
     *
     * @param prefix      the prefix
     * @param makeDaemons the make daemons
     */
    public NamedThreadFactory(String prefix, boolean makeDaemons) {
        this(prefix, 0, makeDaemons);
    }

    public NamedThreadFactory(String prefix, int totalSize) {
        this(prefix, totalSize, true);
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix;
        if (totalSize > 1) {
            name += "_" + totalSize;
        }
        Thread thread = new FastThreadLocalThread(r, name);

        thread.setDaemon(makeDaemons);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
