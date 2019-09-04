package college.springcloud.order.service.impl;


import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.query.Criteria;
import college.springcloud.common.service.BaseService;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author admin
 */
@Service
public class OrderServiceImpl extends BaseService<Order> implements OrderService {

    @Override
    public PageVo<Order> queryOrders(OrderDto orderDto) {
        Criteria<Order, Object> criteria = Criteria.of(Order.class).page(orderDto.getCurrentPage(), orderDto.getPageSize());
        //挺适合业务代码。
//        criteria.andEqualTo(Order::getForderId, orderDto.getForderId())
//                ;
        List<Order> resultList = queryByCriteria(criteria);
        Integer count = countByCriteria(criteria);
        return new PageVo(count, orderDto.getCurrentPage(), orderDto.getPageSize(),
                resultList);
    }
}