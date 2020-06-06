package college.springcloud.hystrix.fallback;

import college.springcloud.hystrix.controller.WebFeginRPCService2;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 11:28
 * Version:V1.0
 */
//@Component
public class WebFeginRPCFallbackService implements WebFeginRPCService2 {
    @Override
    public String getString(String key) {
        return "服务熔断, feign fall back： " + key;
    }
}
