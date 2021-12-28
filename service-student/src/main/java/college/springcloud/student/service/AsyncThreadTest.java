package college.springcloud.student.service;

import college.springcloud.common.exception.BizException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * User: xuxianbei
 * Date: 2019/12/10
 * Time: 15:48
 * Version:V1.0
 */
@Component
public class AsyncThreadTest {

    @Async("singleThreadPool")
    public void asycTest(String key, String key2, Integer key3) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + key + key2 + key3);
    }

    @Async("singleThreadPool")
    public Future<String> asycTestFuture(String key, String key2, Integer key3) {
        Future future = new AsyncResult<>("test");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + key + key2 + key3);
        return future;
    }

    @Async("rpcThreadPool")
    public Integer threadPoolException() {
        throw new BizException("dddd");
    }

}
