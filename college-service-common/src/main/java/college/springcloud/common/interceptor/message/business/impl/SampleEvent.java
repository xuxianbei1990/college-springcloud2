package college.springcloud.common.interceptor.message.business.impl;

import org.springframework.context.ApplicationEvent;

/**
 * @author: xuxianbei
 * Date: 2019/12/24
 * Time: 11:38
 * Version:V1.0
 */
public class SampleEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SampleEvent(Object source) {
        super(source);
    }
}
