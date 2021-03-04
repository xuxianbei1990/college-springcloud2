package college.springcloud.common.annotation;

import java.lang.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 15:10
 * Version:V1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchAuthority {
    boolean required() default true;
}
