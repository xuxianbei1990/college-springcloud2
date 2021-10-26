package college.springcloud.transport.controller;

import college.springcloud.transport.model.StudentVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义序列化
 *
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 11:34
 * Version:V1.0
 */
@RestController
@RequestMapping("serialize")
public class CustomSerializerController {


    /**
     * 定制序列化
     *
     * @param healthy
     * @return
     */
    @GetMapping("student")
    public StudentVo student(Integer healthy) {
        StudentVo studentVo = new StudentVo();
        studentVo.setHealthy(healthy);
        studentVo.setHealthyName("ddf");
        return studentVo;
    }
}
