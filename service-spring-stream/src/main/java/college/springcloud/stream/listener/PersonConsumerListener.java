package college.springcloud.stream.listener;

import college.springcloud.module.Person;
import college.springcloud.stream.IPersonConsumer;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * User: xuxianbei
 * Date: 2019/10/29
 * Time: 9:45
 * Version:V1.0
 */
@EnableBinding(IPersonConsumer.class)
public class PersonConsumerListener {

    @Resource
    private IPersonConsumer personConsumer;

    /**
     * {
     * "name": "xxb"
     * }
     * 默认自带json序列化解析
     *
     * @param person
     */
    @StreamListener(value = IPersonConsumer.PERSON, condition = "headers['name'] == '2'")
    public void consumer(@Payload Person person, @Headers Map map, @Header(AmqpHeaders.CHANNEL) Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        System.out.println(Arrays.toString(map.entrySet().toArray()));
        System.out.println("condition:" + person.toString());
    }

    /**
     * {
     * "name": "xxb"
     * }
     * 默认自带json序列化解析
     *
     * @param person
     */
    @StreamListener(value = IPersonConsumer.PERSON)
    public void consumer2(@Payload Person person, @Headers Map map) {
        System.out.println(Arrays.toString(map.entrySet().toArray()));
        System.out.println("condition2:" + person.toString());
    }

    /**
     * xxb
     *
     * @param name
     */
    @StreamListener(value = IPersonConsumer.INPUTNAME)
    public void consumerName(String name) {
        System.out.println(name);
    }

    /*
      {
      "name": "xxb",
      "age": 5
      }
     */
    @StreamListener(value = IPersonConsumer.PERSON)
    public void producter(Person person) {
        personConsumer.exchangeOutput().send(message(JSONObject.toJSONString(person)));
    }

    @StreamListener(value = IPersonConsumer.PERSON_INTPUT)
    public void inputPerson(Person person) {
        System.out.println("person_output_can_delete" + person.toString());
    }

    private static final <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
}
