package college.springcloud.order.api;


import college.springcloud.common.rpc.Api;
import college.springcloud.order.po.Order;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "xybbc-core-order", path = "order")
public interface OrderApi extends Api<Order> {
}