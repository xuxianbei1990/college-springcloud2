package college.springcloud.order.controller;

import college.springcloud.order.po.Order;
import college.springcloud.order.po.SupplierTransportSku;
import college.springcloud.order.service.OrderService;
import college.springcloud.order.service.SupplierTransportSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 公共类测试
 *
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

    @Autowired
    SupplierTransportSkuService supplierTransportSkuService;

    /**
     * 剔除
     *
     * @return
     */
    @GetMapping("/except")
    public List<Order> criteriaExcept() {
        return orderService.criteriaExcept();
    }

    /**
     * 测试 select * 问题
     *
     * @return
     */
    @GetMapping("/criteria")
    public List<Order> criteria() {
        return orderService.criteria();
    }

    /**
     * 测试 queryByCriteria in 问题
     */
    @GetMapping("/criteria/in")
    public List<SupplierTransportSku> criteriaIn(@RequestParam(required = false, defaultValue = "10")Integer count) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> supplierTransportSkuService.criteriaIn());
        }
        return new ArrayList<>();
    }

}
