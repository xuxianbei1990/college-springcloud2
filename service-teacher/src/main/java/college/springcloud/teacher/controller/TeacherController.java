package college.springcloud.teacher.controller;

import college.springcloud.common.utils.Result;
import college.springcloud.student.api.StudentApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * User: xuxianbei
 * Date: 2019/8/29
 * Time: 9:26
 * Version:V1.0
 *
 * 遗留问题：
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    StudentApi studentApi;

    @GetMapping("/get/test/student")
    public Result getTestStudent() {
        return studentApi.get();
    }

    //测试fegin调用在2.1springboot OpenFegin 是否还需要写@RequestParam
    //测试结果还是需要写。也就是说OpenFegin只是扩展了Rest风格
    @PostMapping("/test/student/insert/param")
    public Result getInsertParam() {
        return studentApi.insert("xxb");
    }
}
