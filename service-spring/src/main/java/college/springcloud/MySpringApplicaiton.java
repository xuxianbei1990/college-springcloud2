package college.springcloud;

import college.springcloud.spring.BeanComponentImport.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * User: xuxianbei
 * Date: 2019/11/5
 * Time: 15:53
 * Version:V1.0
 */
public class MySpringApplicaiton {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        Arrays.stream((applicationContext.getBeanDefinitionNames())).forEach(t -> System.out.println(t));
    }
}
