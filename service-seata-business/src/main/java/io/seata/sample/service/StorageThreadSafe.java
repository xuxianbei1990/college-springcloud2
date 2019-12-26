package io.seata.sample.service;

import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * User: xuxianbei
 * Date: 2019/12/13
 * Time: 11:01
 * Version:V1.0
 */
@EnableAsync
@Component
public class StorageThreadSafe {

    @Resource
    private StorageFeignClient storageFeignClient;

    @Async
    @GlobalTransactional
    public Future<Integer> deductThread(Integer value) {
       storageFeignClient.deduct("C100000", 1);
       if (value == 1) {
            throw new RuntimeException("test thread");
       }
       return new AsyncResult<>(1);
    }

    public Integer deductScope(Integer value) {
        storageFeignClient.deduct("C100000", 2);
        if (value == 1) {
            throw new RuntimeException("test thread");
        }
        return 1;
    }

}
