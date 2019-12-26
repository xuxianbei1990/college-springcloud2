package college.springcloud.producer.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuxianbei
 * Date: 2019/12/20
 * Time: 17:38
 * Version:V1.0
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String send() {
        rabbitTemplate.convertAndSend("xxb-test-can-delete", "hello.world.queue", "Hello World");
        return "1";
    }
}
