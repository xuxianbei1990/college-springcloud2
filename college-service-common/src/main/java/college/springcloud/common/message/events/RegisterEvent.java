package college.springcloud.common.message.events;

import org.springframework.context.ApplicationEvent;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:33
 * Version:V1.0
 */
public class RegisterEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RegisterEvent(Object source) {
        super(source);
    }
}
