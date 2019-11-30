package college.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 16:51
 * Version:V1.0
 */
@SpringBootApplication
@MapperScan({"college.springcloud.notify.mapper"})
public class NotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyApplication.class, args);
    }
}
