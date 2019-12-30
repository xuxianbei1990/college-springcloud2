package college.springcloud.controller;

import college.springcloud.stream.producter.MessagePushProducter;
import college.springcloud.stream.producter.MsgPushDto;
import college.springcloud.stream.rabbit.RPersonConsumerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: xuxianbei
 * Date: 2019/10/31
 * Time: 11:36
 * Version:V1.0
 */
@RestController
@RequestMapping("/producter")
public class PersonProducterController {

    @Autowired
    RPersonConsumerListener personConsumerListener;

    @Autowired
    MessagePushProducter messagePushProducter;

    @PostMapping("/send/person")
    public Integer sendPerson() {
        return personConsumerListener.personProducter() ? 1 : 0;
    }

    @PostMapping("/send/message")
    public String sendMessage(@RequestBody MsgPushDto msgPushDto) {
        messagePushProducter.setMessagePushChannel(msgPushDto);
        return "1";
    }

}
