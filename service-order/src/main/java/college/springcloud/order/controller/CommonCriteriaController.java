package college.springcloud.order.controller;

import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共类测试
 * @author: xuxianbei
 * Date: 2020/1/7
 * Time: 9:53
 * Version:V1.0
 */
@RestController
@RequestMapping("/criteria")
public class CommonCriteriaController {


    @Autowired
    OrderService orderService;

    /**
     * 剔除
     * @return
     */
    @GetMapping("/except")
    public List<Order> criteriaExcept() {
        return orderService.criteriaExcept();
    }

    @GetMapping("/criteria")
    public List<Order> criteria() {
        return orderService.criteria();
    }
}
