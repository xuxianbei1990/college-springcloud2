package college.springcloud.hystrix.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 11:31
 * Version:V1.0
 * 使用FeignClient的降级
 */
//@FeignClient(value = "college-service-student", path = "/hystrix", fallback = WebFeginRPCFallbackService.class)
public interface WebFeginRPCService2 extends RPCService {

    @RequestMapping(value = "/getString2", method = RequestMethod.GET)
    String getString(@RequestParam("key") String key);
}
