package college.springcloud.spring.aeventdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Name
 *
 * @author xxb
 * Date 2019/4/17
 * VersionV1.0
 * @description
 */
@RestController
@RequestMapping("/publish")
public class PulishController {

    @Autowired
    DemoEventPublisher demoEventPublisher;

    @Resource
    private SimpleApplicationEventMulticaster simpleApplicationEventMulticaster;

    @Resource
    private AsyncTaskExecutor taskExecutor;

    @GetMapping("/test")
    public String doPublish(String msg, String email) {
        simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor);
        System.out.println(Thread.currentThread());
        demoEventPublisher.publish(msg, email);
        return "1";
    }

}
