package college.springcloud.common.async;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author: xuxianbei
 * Date: 2021/12/8
 * Time: 20:25
 * Version:V1.0
 */
public class MyThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Future<T> result = super.submit(task);
//        try {
//            result.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return result;
    }
}
