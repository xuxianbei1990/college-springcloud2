package college.springcloud.annotation;

import college.springcloud.enums.CodeNameInterface;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 11:43
 * Version:V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoExchange {
     Class value();

}
