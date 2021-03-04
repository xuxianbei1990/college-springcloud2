package college.springcloud.log.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 14:10
 * Version:V1.0
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {


    @PostConstruct
    void init(){
        log.info("init");
    }

    @GetMapping("/test")
    public String testLog() {
        log.info("tewsst");
        return "success";
    }
}
