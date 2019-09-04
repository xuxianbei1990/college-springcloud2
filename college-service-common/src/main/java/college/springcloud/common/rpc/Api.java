package college.springcloud.common.rpc;

import college.springcloud.common.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 16:52
 * Version:V1.0
 */
public interface Api<T> {

    @GetMapping({"/sayHello"})
    Result<String> sayHello(@RequestParam("name") String name);

    @GetMapping("/queryAll")
    Result<List<T>> queryAll();
}
