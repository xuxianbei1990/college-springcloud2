package college.springcloud.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 9:36
 * Version:V1.0
 */
@SpringBootApplication(scanBasePackages = {"college.springcloud"})
//说明这个功能
@EnableFeignClients("college.springcloud.*.api")
@EnableDiscoveryClient
@EnableAsync
public class StudentApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentApplication.class, args);
    }
}
