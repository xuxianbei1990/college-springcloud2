package college.springcloud.spring.aeventdemo;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Name
 *
 * @author xxb
 * Date 2019/4/17
 * VersionV1.0
 * @description
 */
@Getter
public class DemoEvent extends ApplicationEvent {
    private String msg;

    private String email;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DemoEvent(Object source, String msg, String email) {
        super(source);
        this.email = email;
        this.msg = msg;
    }
}
