package college.springcloud.gateway.limiter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

/**
 * @author: xuxianbei
 * Date: 2020/6/12
 * Time: 20:12
 * Version:V1.0
 * https://www.cnblogs.com/csts/p/10286378.html
 */
//@Configuration
//@Import({HostAddrKeyResolver.class, UriKeyResolver.class})
public class LimiterConfig {

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}
