package college.springcloud.hystrix.controller;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 11:59
 * Version:V1.0
 */
public interface RPCService {
    String getString(@RequestParam("key") String key);
}
