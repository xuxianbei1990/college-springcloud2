package college.springcloud.common.config.IdempotentInterceptorAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定制非幂等
 * @author: xuxianbei
 * Date: 2021/1/22
 * Time: 16:28
 * Version:V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotIdempotent {
}
