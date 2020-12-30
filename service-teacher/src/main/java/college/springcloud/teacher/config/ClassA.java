package college.springcloud.teacher.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuxianbei
 * Date: 2020/12/29
 * Time: 19:30
 * Version:V1.0
 */
@Configuration
@AutoConfigureAfter(ClassB.class)
public class ClassA implements InitializingBean {

    @Value("${server.port}")
    private Integer port;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("ClassA");
    }
}
