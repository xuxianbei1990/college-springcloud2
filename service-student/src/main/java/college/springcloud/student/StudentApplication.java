package college.springcloud.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

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
@MapperScan("/college/springcloud/student/mapper")
public class StudentApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentApplication.class, args);
    }
}
