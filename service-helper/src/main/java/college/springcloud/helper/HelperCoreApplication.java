/**
 *
 */
package college.springcloud.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * @author bond
 *
 */
@SpringCloudApplication
@EnableFeignClients("college.springcloud.helper.*.api")
@ComponentScan(basePackages = "college.springcloud.helper")
@EnableAutoConfiguration
public class HelperCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelperCoreApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
