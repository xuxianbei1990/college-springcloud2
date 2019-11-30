package college.springcloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * User: xuxianbei
 * Date: 2019/10/29
 * Time: 9:40
 * Version:V1.0
 */
public interface IPersonConsumer {
    //绑定配置文件的交换机
    String PERSON = "person";
    String INPUTNAME = "person_name_input";
    String EXCHANGE = "person_output";
    String PERSON_INTPUT = "person_intput";


    @Input(PERSON)
    SubscribableChannel person_input2();

    @Input(IPersonConsumer.INPUTNAME)
    SubscribableChannel person_name2();

    @Output(EXCHANGE)
    MessageChannel exchangeOutput();

    @Input(PERSON_INTPUT)
    SubscribableChannel person_input();
}
