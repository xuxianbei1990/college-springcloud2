package college.springcloud.stream.producter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * @author: xuxianbei
 * Date: 2019/12/27
 * Time: 17:51
 * Version:V1.0
 */
@EnableBinding(MessagePushChannel.class)
@Slf4j
public class MessagePushProducter {

    @Resource
    MessagePushChannel messagePushChannel;

    public void setMessagePushChannel(MsgPushDto msgPushDto) {
        messagePushChannel.systemNoticeOut().send(MessageBuilder.withPayload(msgPushDto).build());
    }


}
