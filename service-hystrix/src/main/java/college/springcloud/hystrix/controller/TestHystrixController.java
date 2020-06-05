package college.springcloud.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: xuxianbei
 * Date: 2019/7/22
 * Time: 9:57
 * Version:V1.0
 * 正常情况下设置超时时间，需要压测接口平均调用时间，可以使用jmeter。
 */
@RestController
@RequestMapping("/test")
public class TestHystrixController {

    @Autowired
    WebFeginRPCService feginRPCService;

    /**
     * 1.超时直接调用studentFallback
     * 2.错误请求数占总请求数的比例默认大约是50%时候，触发熔断。这个我不太明白触发熔断是指后续的请求都不会执行了？
     * 3. 为每个依赖提供一个小的线程池（或信号），如果线程池已满调用将被立即拒绝，默认不采用排队.加速失败判定时间。
     * 4. 请求失败(异常，拒绝，超时，短路)时执行fallback(降级)逻辑。
     * 5. 提供熔断器组件,可以自动运行或手动调用,停止当前依赖一段时间(10秒)，熔断器默认错误率阈值为50%,超过将自动运行。
     * 6. 提供近实时依赖的统计和监控
     **/
    //简单测试，就是把服务停用了.
    @GetMapping("/student")
    @HystrixCommand(fallbackMethod = "studentFallback")
    public String student() {
        return feginRPCService.getString("student");
    }

    public String studentFallback() {
        return "服务被熔断";
    }

}
