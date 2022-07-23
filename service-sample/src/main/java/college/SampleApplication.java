package college;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: xuxianbei
 * Date: 2022/4/19
 * Time: 17:56
 * Version:V1.0
 */
@SpringBootApplication(scanBasePackages = {"college.sample"})
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
