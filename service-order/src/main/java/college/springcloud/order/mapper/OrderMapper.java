package college.springcloud.order.mapper;

import college.springcloud.common.db.BbcMapper;
import college.springcloud.order.po.Order;

public interface OrderMapper extends BbcMapper<Order> {

    Integer updateOrderStatus(Order order);
}