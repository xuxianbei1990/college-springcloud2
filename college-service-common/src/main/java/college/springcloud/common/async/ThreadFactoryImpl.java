package college.springcloud.common.async;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 * @author: xuxianbei
 * Date: 2020/11/25
 * Time: 15:37
 * Version:V1.0
 */
public class ThreadFactoryImpl implements ThreadFactory {

    private String threadNamePrefix;

    private AtomicInteger threadIndex = new AtomicInteger();

    public ThreadFactoryImpl(final String threadNamePrefix){
        this.threadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadNamePrefix + threadIndex.incrementAndGet());
        //这种写法在spring注入时候如果是 返回结果时候是无效的，要通过get实现
        thread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            System.out.println("test==============");
            throw new RuntimeException(e);
        });
        return thread;
    }
}
