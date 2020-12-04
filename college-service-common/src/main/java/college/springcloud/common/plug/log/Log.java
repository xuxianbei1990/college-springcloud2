package college.springcloud.common.plug.log;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2020/12/1
 * Time: 11:46
 * Version:V1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Log {
}
