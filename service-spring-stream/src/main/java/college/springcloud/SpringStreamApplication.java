package college.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User: xuxianbei
 * Date: 2019/10/28
 * Time: 15:59
 * Version:V1.0
 */
@SpringBootApplication(scanBasePackages = "college.springcloud")
public class SpringStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringStreamApplication.class, args);
    }
}
