package college.springcloud.hystrix.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: xuxianbei
 * Date: 2019/7/22
 * Time: 10:00
 * Version:V1.0
 */
@FeignClient(value = "college.springcloud.service-web-fegin", path = "/fegin")
public interface WebFeginRPCService {

    @RequestMapping(value = "/getString", method = RequestMethod.GET)
    String getString(@RequestParam("key") String key);
}
