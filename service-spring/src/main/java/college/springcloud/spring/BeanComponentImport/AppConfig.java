package college.springcloud.spring.BeanComponentImport;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
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
