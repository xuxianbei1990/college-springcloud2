package college.springcloud.teacher.config;

import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuxianbei
 * Date: 2020/12/29
 * Time: 10:32
 * Version:V1.0
 */
@Configuration
public class FeignPrintServerConfiguration {


//    @Bean
    public IRule ribbonRule() {
//        PredicateBasedRule rule = new PrintZoneAvoidanceRule();
        //这里是处理高可用代码的，目前系统不存在分区情况
//        rule.initWithNiwsConfig();
        return new RandomRule();
    }

}
