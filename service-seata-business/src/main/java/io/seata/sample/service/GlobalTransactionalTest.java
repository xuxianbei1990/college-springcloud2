package io.seata.sample.service;

import io.seata.sample.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: xuxianbei
 * Date: 2019/10/18
 * Time: 15:04
 * Version:V1.0
 */
@Service
public class GlobalTransactionalTest {

    @Resource
    private StorageFeignClient storageFeignClient;

    @GlobalTransactional
    public String query() {
       String result = storageFeignClient.query("");
       return  result;
    }
}
