package college.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: xuxianbei
 * Date: 2020/6/11
 * Time: 11:08
 * Version:V1.0
 */
//该代理使用Ribbon来定位注册到eureka server上的微服务，同时，整合了hystrix，实现了容错。
//@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient

public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }

//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        builder.routes().route()
//    }
}
