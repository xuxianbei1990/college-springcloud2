package college.springcloud.common.config.IdempotentInterceptorAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定制幂等
 * @author: xuxianbei
 * Date: 2021/1/5
 * Time: 13:37
 * Version:V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
}
