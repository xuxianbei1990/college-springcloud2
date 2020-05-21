package college.springcloud.spring.BeanComponentImport;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 注入：IOC 的方式；1.直接注入类；2.字符串形式。3.直接定义BeanDefinition
 * User: xuxianbei
 * Date: 2019/11/5
 * Time: 15:57
 * Version:V1.0
 * 那我是不是可以用这个做一个类似链路监控工具
 */
@Configuration
@Import({Apple.class, Banana.class, BerryImportSelector.class, TomatoRegistrar.class})
public class AppConfig {
}
