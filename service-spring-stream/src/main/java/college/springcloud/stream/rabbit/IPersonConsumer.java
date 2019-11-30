package college.springcloud.stream.rabbit;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * User: xuxianbei
 * Date: 2019/10/30
 * Time: 18:08
 * Version:V1.0
 */
public interface IPersonConsumer {
    String RPERSONINPUT = "rperson_input";

    @Input(RPERSONINPUT)
    SubscribableChannel rperson_input();

    //延迟队列
    String PERSONDELAYINPUT = "person_delay_input";
    String PERSONDELAYOUTPUT = "person_delay_output";


    //延迟队列
    @Input(PERSONDELAYINPUT)
    SubscribableChannel person_delay_input();

    @Output(PERSONDELAYOUTPUT)
    MessageChannel person_delay_output();
}
