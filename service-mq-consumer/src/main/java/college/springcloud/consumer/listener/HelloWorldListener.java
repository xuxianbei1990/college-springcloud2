package college.springcloud.consumer.listener;

import college.springcloud.consumer.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: xuxianbei
 * Date: 2019/12/20
 * Time: 17:42
 * Version:V1.0
 */
@Component
@RabbitListener(queues = RabbitMqConfig.helloWorldQueueName)
public class HelloWorldListener {

    @RabbitHandler
    public void process(Object object) throws IOException {
        if (object instanceof Message) {
            System.out.println("receive:==" + new String(((Message) object).getBody()));
        }
//        System.out.println("receive:==" + object);
    }
}
