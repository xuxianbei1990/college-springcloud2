package college.springcloud.teacher.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author: xuxianbei
 * Date: 2020/12/29
 * Time: 19:32
 * Version:V1.0
 */
@Configuration
public class ClassB implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ClassB");
    }
}
