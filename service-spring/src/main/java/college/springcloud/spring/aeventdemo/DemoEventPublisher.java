package college.springcloud.spring.aeventdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Name
 *
 * @author xxb
 * Date 2019/4/17
 * VersionV1.0
 * @description  用这个实现类的注册功能
 */
@Component
public class DemoEventPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void publish(String msg, String email) {
        applicationContext.publishEvent(new DemoEvent(this, msg, email));
    }

}
