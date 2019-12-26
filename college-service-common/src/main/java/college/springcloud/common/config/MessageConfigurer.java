package college.springcloud.common.config;

import college.springcloud.common.interceptor.message.MessageInterceptor;
import college.springcloud.common.interceptor.message.filter.RepeatReadFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 9:23
 * Version:V1.0
 */
@Configuration
public class MessageConfigurer implements WebMvcConfigurer {

    @Autowired
    MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(messageInterceptor);
    }


    @Bean
    public FilterRegistrationBean repeatedlyReadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        RepeatReadFilter repeatedlyReadFilter = new RepeatReadFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
