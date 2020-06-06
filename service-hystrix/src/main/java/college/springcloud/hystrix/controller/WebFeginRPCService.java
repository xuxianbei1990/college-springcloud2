package college.springcloud.hystrix.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: xuxianbei
 * Date: 2019/7/22
 * Time: 10:00
 * Version:V1.0
 * 1.指定服务器名称加路径
 * 2.url:指定ip "localhost:8001"
 */
//@FeignClient(value = "college-service-student", path = "/hystrix")
public interface WebFeginRPCService extends RPCService {

    @RequestMapping(value = "/getString", method = RequestMethod.GET)
    String getString(@RequestParam("key") String key);
}
