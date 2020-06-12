package college.springcloud.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuxianbei
 * Date: 2020/6/6
 * Time: 10:27
 * Version:V1.0
 */
@RestController
@RequestMapping("/hystrix")
public class TestHystrixController {

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/getString")
    public String getString(String key) {
        return key + applicationContext.getEnvironment().getProperty("server.port");
    }
}
