package college.springcloud.teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * User: xuxianbei
 * Date: 2019/8/28
 * Time: 20:33
 * Version:V1.0
 */
@SpringBootApplication(scanBasePackages = "college.springcloud")
@EnableDiscoveryClient
//这里说明服务还是调用方触发，只不过服务的代码改为服务发布方编写
//好处就是：控制变化。这样调用方就不用关心服务发布方的内部变化的，增加代码内聚
@EnableFeignClients("college.springcloud.*.api")
public class TeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, args);
    }
}
