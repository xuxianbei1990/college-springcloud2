package college.springcloud.log.plug.log.config;


import college.springcloud.log.plug.log.AbstractLog;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * 拦截Feign
 */
public class FeignInterceptorConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header(AbstractLog.SHORT_TRACE_ID, AbstractLog.getTraceId());
    }
}