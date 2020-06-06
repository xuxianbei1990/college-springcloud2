package college.springcloud.hystrix.controller;

import college.springcloud.hystrix.fallback.WebFeginRPCFactoryServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 11:32
 * Version:V1.0
 */
@FeignClient(value = "college-service-student", path = "/hystrix",  fallbackFactory = WebFeginRPCFactoryServiceFallback.class)
public interface WebFeginRPCFactoryService extends RPCService{

    @RequestMapping(value = "/getString/factory", method = RequestMethod.GET)
    String getString(@RequestParam("key") String key);
}
