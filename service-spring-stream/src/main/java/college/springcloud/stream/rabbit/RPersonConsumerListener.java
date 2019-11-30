package college.springcloud.stream.rabbit;

import college.springcloud.module.Person;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * User: xuxianbei
 * Date: 2019/10/30
 * Time: 21:03
 * Version:V1.0
 */
@EnableBinding(IPersonConsumer.class)
public class RPersonConsumerListener {

    @Resource
    IPersonConsumer iPersonConsumer;

    private int count = 0;

    /*
      {
      "name": "xxb",
      "age": 5
      }
     */

    @StreamListener(IPersonConsumer.RPERSONINPUT)
    public void rpersonInput(@Payload Person person, @Header(AmqpHeaders.CHANNEL) Channel channel,
                             @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        //用来测试手动应答是否生效
        count++;
        System.out.println(person.toString());
        if (count > 5) {
            count = 0;
            //true 应答所有； false 应答当次
            channel.basicAck(deliveryTag, false);
        } else {
            // 拒绝消息重新返回队列
            channel.basicNack(deliveryTag, false, true);
        }
    }

    @StreamListener(IPersonConsumer.PERSONDELAYINPUT)
    public void personDelayInput(Person person) {
        System.out.println("延迟队列：" + person.toString());
    }

    public boolean personProducter() {
        Person person = new Person();
        person.setAge(9000);
        person.setName("E.N.D");
        return iPersonConsumer.person_delay_output().send(MessageBuilder.withPayload(person)
                //我记得可以设置队列整个为延期的。不过这已经可以解决问题了。不需要浪费时间了
                .setHeader("x-delay", 5000).build());
    }
}
