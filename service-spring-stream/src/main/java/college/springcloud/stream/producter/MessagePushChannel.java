package college.springcloud.stream.producter;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/** 生产者和消费者不能定义一起。在实际生产过程中
 * spring机构中：@EnableBing 和 @Input 就可以产生一个监听者
 * @author nick
 * @version 1.0.0
 * @date 2019-12-20
 * 消息推送channel
 * @copyright 本内容仅限于深圳市天行云供应链有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MessagePushChannel {

    /**
     * 站内通知模块
     */
    String WEBSITE_NOTICE_OUT = "website_notice_out";

    String WEBSITE_NOTICE_INPUT = "website_notice_input";

    /**
     * 手动推送模块
     */
    String OPT_NOTICE_INPUT = "opt_notice_input";

    String OPT_NOTICE_OUT = "opt_notice_out";

    /**
     * 模板设置模块
     */
    String SYSTEM_NOTICE_INPUT = "system_notice_input";

    String SYSTEM_NOTICE_OUT = "system_notice_out";

//    @Input(WEBSITE_NOTICE_INPUT)
//    SubscribableChannel websiteNoticeInput();


    @Output(WEBSITE_NOTICE_OUT)
    MessageChannel websiteNoticeOut();

    @Output(SYSTEM_NOTICE_OUT)
    MessageChannel systemNoticeOut();

    @Output(OPT_NOTICE_OUT)
    SubscribableChannel optNoticeOut();

}
