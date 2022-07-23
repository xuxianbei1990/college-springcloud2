package college.springcloud.ribbon.rpc;

import college.springcloud.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: xuxianbei
 * Date: 2021/11/24
 * Time: 11:09
 * Version:V1.0
 */
@FeignClient(value = "college-service-order")
public interface OrderServiceRPC {

    @GetMapping("/order/order/id")
    Result<Integer> orderId();
}
