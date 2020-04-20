package college.springcloud.order.controller;

import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.utils.Result;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.dto.OrderVoDto;
import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import college.springcloud.order.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2020/4/3
 * Time: 14:10
 * Version:V1.0
 */
@RestController
@RequestMapping("/serialize")
public class SerializeController {

    @Autowired
    OrderService orderService;

    @GetMapping("/query/page")
    public Result<PageVo<OrderVo>> queryOrders(OrderDto orderDto) {
        PageVo<Order> pageVo = orderService.queryOrders(orderDto);
        PageVo<OrderVo> resuel = new PageVo(pageVo.getTotalCount(),
                pageVo.getCurrentPage(), pageVo.getPageSize(), pageVo.getList().stream().map(t -> {
            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(t, orderVo);
            orderVo.setForderAmountBig(BigDecimal.valueOf(t.getForderAmount()));
            return orderVo;
        }).collect(Collectors.toList()));
        return Result.success(resuel);
    }

    @PostMapping("/query")
    public Result<OrderVoDto> queryOrders(@RequestBody @Validated OrderVoDto orderDto) {
        System.out.println(orderDto.getList());
        return Result.success(orderDto);
    }
}
