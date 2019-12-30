package college.springcloud.transport.controller;

import college.springcloud.common.utils.OkHttpUtil;
import college.springcloud.transport.model.OkHttpEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2019/12/30
 * Time: 15:44
 * Version:V1.0
 */
@RestController
@RequestMapping("/okHttp")
public class OkHttpController {

    @Autowired
    OkHttpUtil okHttpUtil;

    @PostMapping("/receiveJson")
    public Object receiveJsonController(@RequestBody OkHttpEntity okHttpEntity) {
        return "receive:" + okHttpEntity.toString();
    }

    @GetMapping("/testJson")
    public String testOkhttpController() {
        OkHttpEntity okHttpEntity = new OkHttpEntity();
        okHttpEntity.setId("x");
        okHttpEntity.setKey("sdf");
        return okHttpUtil.postJsonParams("http://localhost:6032/okHttp/receiveJson", JSONObject.toJSONString(okHttpEntity));
    }
}
