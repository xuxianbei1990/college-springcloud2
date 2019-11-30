package college.springcloud.order.controller;

import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.rpc.BaseApi;
import college.springcloud.common.utils.Result;
import college.springcloud.order.api.OrderApi;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController extends BaseApi<Order> implements OrderApi {

    @Autowired
    OrderService orderService;

    @GetMapping("/query/page")
    public Result<PageVo<Order>> queryOrders(OrderDto orderDto) {
        return Result.success(orderService.queryOrders(orderDto));
    }

    //测试乐观锁
    @PostMapping("/update")
    public Result<Object> updateOrders(@RequestBody OrderDto orderDto) {
        return Result.success(orderService.updateOrders(orderDto));
    }

    //测试乐观锁
    @PostMapping("/update2")
    public Result<Object> updateOrders2(@RequestBody OrderDto orderDto) {
        return Result.success(orderService.updateOrders2(orderDto));
    }

    //测试批量更新 case when
    @GetMapping("/batch/update")
    public Result<Integer> batchUpdate() {
        return Result.success(orderService.batchUpdate());
    }

    //测试批量更新 foreache
    @GetMapping("/batch/update/test")
    public Result<Integer> batchUpdate2() {
        return Result.success(orderService.batchUpdateTest());
    }

    //测试like
    @GetMapping("/query/like")
    public Result<Object> query() {
        return Result.success(orderService.query());
    }

    public static void main(String[] args) {

    }

    //测试insertList
    @GetMapping("/insertList")
    public Result<Integer> insertList() {
        return Result.success(orderService.createBatch());
    }
}