package college.springcloud.student.annotation;

import java.lang.annotation.*;

/**
 * User: xuxianbei
 * Date: 2019/9/7
 * Time: 15:06
 * Version:V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TeacherRoles.class)
public @interface TeacherRole {
    String value() default "";
}
