package college.springcloud.common.config;

import college.springcloud.log.plug.log.config.FeignInterceptorConfig;
import college.springcloud.log.plug.log.config.FilterConfig;
import college.springcloud.log.plug.log.config.LogUrlConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 11:44
 * Version:V1.0
 */
@Configuration
public class DefaultLogConfiguration {
    @Bean
    public LogUrlConfig logUrlConfig() {
        return new LogUrlConfig();
    }

    @Bean
    public FilterConfig filterConfig() {
        return new FilterConfig();
    }


    @Bean
    public FeignInterceptorConfig feignInterceptorConfig() {
        return new FeignInterceptorConfig();
    }
}
