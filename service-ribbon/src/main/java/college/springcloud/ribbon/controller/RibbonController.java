package college.springcloud.ribbon.controller;

import college.springcloud.common.utils.Result;
import college.springcloud.ribbon.rpc.OrderServiceRPC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: xuxianbei
 * Date: 2021/11/23
 * Time: 19:54
 * Version:V1.0
 */
@RestController
public class RibbonController {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    OrderServiceRPC orderServiceRPC;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("ribbon/loadBalance")
    public String findById() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("college-service-order");
        return restTemplate.getForObject(serviceInstance.getUri() + "/order/order/id", Result.class).toString();
    }

    @GetMapping("ribbon/loadBalance/feign")
    public String findByIdFeign() {
        return orderServiceRPC.orderId().toString();
    }
}
