package college.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.internal.EnableZipkinServer;

/**
 * User: xuxianbei
 * Date: 2019/11/5
 * Time: 9:54
 * Version:V1.0
 */
@SpringBootApplication
@EnableZipkinServer
public class SleuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleuthApplication.class, args);
    }
}
