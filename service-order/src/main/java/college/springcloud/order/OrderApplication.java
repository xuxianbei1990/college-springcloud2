package college.springcloud.order;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
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
@SpringBootApplication(scanBasePackages = {"com.alicp.jetcache.autoconfigure", "college.springcloud.order"})
@EnableMethodCache(basePackages = "college.springcloud.order.service.impl")
@EnableDiscoveryClient
@EnableCreateCacheAnnotation
@MapperScan({"college.springcloud.order.mapper"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
