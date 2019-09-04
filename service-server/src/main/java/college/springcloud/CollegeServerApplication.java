package college.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * User: EDZ
 * Date: 2019/8/26
 * Time: 19:35
 * Version:V1.0
 */
@SpringBootApplication
@EnableEurekaServer
public class CollegeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollegeServerApplication.class, args);
    }
}
