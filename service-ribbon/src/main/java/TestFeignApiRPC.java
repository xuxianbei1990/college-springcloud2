import college.springcloud.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: xuxianbei
 * Date: 2020/6/10
 * Time: 17:32
 * Version:V1.0
 */
@FeignClient(value = "college-service-student", path = "student")
public interface TestFeignApiRPC {

    @GetMapping("/get")
    Result get();
}
