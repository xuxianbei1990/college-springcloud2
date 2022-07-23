package college.springcloud.common.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * User: xuxianbei
 * Date: 2019/12/10
 * Time: 14:13
 * Version:V1.0
 */
@Component
@Slf4j
public class CustomTaskExecutor {

    @Bean
    public TaskExecutor asyncTaskThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(5);
        // 设置最大线程数
        executor.setMaxPoolSize(50);
        executor.setRejectedExecutionHandler((r, executor1) ->
                log.info("insertUnusualOrder错误:{} From : {} ", r.toString(), executor1.toString()));
        // 设置队列容量
        executor.setQueueCapacity(200);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("添加异常单线程");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 等待所有任务结束后再关闭线程池
        //executor.setWaitForTasksToCompleteOnShutdown(true);
        //当为bean的时候不需要调用此方法，装载容器的时候回自动调用
//        executor.initialize();
        return executor;
    }

    @Bean("singleThreadPool")
    public TaskExecutor asyncSingleTaskThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(1);
        // 设置最大线程数
        executor.setMaxPoolSize(1);
//        executor.setRejectedExecutionHandler((r, executor1) ->
//                log.info("insertUnusualOrder错误:{} From : {} ", r.toString(), executor1.toString()));
        // 设置队列容量
        executor.setQueueCapacity(500);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(0);
        // 设置默认线程名称
        executor.setThreadNamePrefix("单线程");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 等待所有任务结束后再关闭线程池
        //executor.setWaitForTasksToCompleteOnShutdown(true);
        //当为bean的时候不需要调用此方法，装载容器的时候回自动调用
//        executor.initialize();
        return executor;
    }

    /**
     * rpc远程调用线程池
     *
     * @return
     */
    @Bean("rpcThreadPool")
    public AsyncTaskExecutor rpcThreadPool() {
        MyThreadPoolTaskExecutor executor = new MyThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 设置最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 3);
        // 设置队列容量 默认就是阻塞队列
        executor.setQueueCapacity(5000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(10);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 处理异常
        executor.setThreadFactory(new ThreadFactoryImpl("远程调用线程"));
        return executor;
    }


    /**
     * 本地并发访问sql
     * 缺点：给数据承担压力
     *
     * @return
     */
    @Bean("sqlThreadPool")
    public AsyncTaskExecutor sqlThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 设置最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        // 设置队列容量 默认就是阻塞队列
        executor.setQueueCapacity(100);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(30);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 处理异常
        executor.setThreadFactory(new ThreadFactoryImpl("本地访问sql线程"));
        return executor;
    }


    /**
     * 定时清理内存线程池
     *
     * @return
     */
    @Bean("sheduleClearMemory")
    public TaskExecutor sheduleClearMemoryThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 1);
        // 设置最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 1);
        // 设置队列容量 默认就是阻塞队列
        executor.setQueueCapacity(5000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(0);
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 处理异常
        executor.setThreadFactory(new ThreadFactoryImpl("定时清理内存"));
        return executor;
    }
}
