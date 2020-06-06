package college.springcloud.student.controller;

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

    @GetMapping("/getString")
    public String getString(String key) {
        return key;
    }
}
