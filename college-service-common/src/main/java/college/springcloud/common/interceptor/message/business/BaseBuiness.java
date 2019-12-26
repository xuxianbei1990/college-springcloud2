package college.springcloud.common.interceptor.message.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import javax.annotation.PostConstruct;

/** 业务基类
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 16:51
 * Version:V1.0
 */
public class BaseBuiness extends ApplicationEvent {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public BaseBuiness(Object source) {
        super(source);
    }

    @PostConstruct
    public void publish(){
        applicationContext.publishEvent(this);
    }
}
