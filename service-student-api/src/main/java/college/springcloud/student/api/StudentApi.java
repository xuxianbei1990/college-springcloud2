package college.springcloud.student.api;

import college.springcloud.common.utils.Result;
import college.springcloud.student.fallback.StudentApiFallBack;
import college.springcloud.student.po.Student;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:19
 * Version:V1.0
 */
@FeignClient(value = "college-service-student", path = "student", fallback = StudentApiFallBack.class)
public interface StudentApi {

    @GetMapping("/get")
    Result get();

    @PostMapping("/insert/param")
    Result insert(@RequestParam("param") String param);

    @GetMapping("/get/Student")
    Result getStudent(Student student);

    @GetMapping("/get/Student/map")
    Result getStudentQueryMap(@QueryMap Student student);
}
