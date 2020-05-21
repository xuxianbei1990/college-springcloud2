package college.springcloud.aliyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: xuxianbei
 * Date: 2020/5/20
 * Time: 9:54
 * Version:V1.0
 */
@SpringBootApplication(scanBasePackages = "college.springcloud")
public class AliyunApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliyunApplication.class);
    }
}
