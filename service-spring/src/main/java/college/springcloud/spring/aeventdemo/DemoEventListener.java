package college.springcloud.spring.aeventdemo;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Name
 *
 * @author xxb
 * Date 2019/4/17
 * VersionV1.0
 * @description
 */
@Component
public class DemoEventListener implements ApplicationListener<DemoEvent> {
    //使用注解@Async支持 这样不仅可以支持通过调用，也支持异步调用，非常的灵活，
//    @Async
    @Override
    public void onApplicationEvent(DemoEvent event) {
        System.out.println("注册成功，发送确认邮件为：" + event.getEmail() + ",消息摘要为:" + event.getMsg());
    }

    //如果用这个不用 implements ApplicationListener
    @EventListener
    public void listen(DemoEvent event) {
        System.out.println("注册成功，发送确认邮件为：" + event.getEmail() + ",消息摘要为:" + event.getMsg());
    }
}
