package college.springcloud.spring.aeventdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Name
 *
 * @author xxb
 * Date 2019/4/17
 * VersionV1.0
 * @description
 */
@RestController
@RequestMapping("/publish")
public class PulishController {

    @Autowired
    DemoEventPublisher demoEventPublisher;

    @GetMapping("/test")
    public String doPublish(String msg, String email) {
        demoEventPublisher.publish(msg, email);
        return "1";
    }

}
