package college.springcloud.hystrix.fallback;

import college.springcloud.common.utils.ResultUtils;
import college.springcloud.hystrix.controller.WebFeginRPCFactoryService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 11:35
 * Version:V1.0
 * FallBackFactory 比 FallBack好处：就是增加了异常信息处理
 */
@Slf4j
@Component
public class WebFeginRPCFactoryServiceFallback implements FallbackFactory<WebFeginRPCFactoryService> {
    @Override
    public WebFeginRPCFactoryService create(Throwable cause) {
        log.info(ResultUtils.getStackTrace(cause));
        return (key -> "服务熔断, factory fall back： " + key);
    }
}
