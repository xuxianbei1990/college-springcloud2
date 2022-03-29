package college.springcloud.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: xuxianbei
 * Date: 2021/11/23
 * Time: 19:53
 * Version:V1.0
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "college.springcloud.ribbon.rpc")
@SpringBootApplication
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }
}
