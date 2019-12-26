package college.springcloud.common.message.listeners;

import college.springcloud.common.message.events.RegisterEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:17
 * Version:V1.0
 */
@Component
public class MessageRegisterListener implements ApplicationListener<RegisterEvent> {

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        System.out.println(event.getSource());
        //
        System.out.println("发送mq");
    }
}
