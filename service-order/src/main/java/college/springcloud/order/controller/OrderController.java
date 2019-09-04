package college.springcloud.order.controller;

import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.rpc.BaseApi;
import college.springcloud.common.utils.Result;
import college.springcloud.order.api.OrderApi;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController extends BaseApi<Order> implements OrderApi {

    @Autowired
    OrderService orderService;

    @GetMapping("/query/page")
    public Result<PageVo<Order>> queryOrders(OrderDto orderDto) {
        return Result.success(orderService.queryOrders(orderDto));
    }

}