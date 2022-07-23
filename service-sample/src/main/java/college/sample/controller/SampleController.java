package college.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuxianbei
 * Date: 2022/4/19
 * Time: 18:00
 * Version:V1.0
 */
@RestController
@RequestMapping("/sample")
public class SampleController {

    private Integer idInc = 1;

    @GetMapping("id/test")
    public Integer sample(Integer id) {
        return idInc++;
    }
}
