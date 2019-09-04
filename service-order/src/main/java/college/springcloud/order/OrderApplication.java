package college.springcloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * User: xuxianbei
 * Date: 2019/8/29
 * Time: 11:43
 * Version:V1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("college.springcloud.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
