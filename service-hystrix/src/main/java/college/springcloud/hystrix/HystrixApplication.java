package college.springcloud.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * User: xuxianbei
 * Date: 2019/7/22
 * Time: 9:27
 * Version:V1.0
 * 熔断即调用的服务器的压力太大发生的。
 * 本实例是把service-web-fegin 作为服务被调用方
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class HystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
}
