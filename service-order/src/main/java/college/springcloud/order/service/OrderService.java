package college.springcloud.order.service;


import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.service.IService;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.po.Order;

public interface OrderService extends IService<Order> {

   PageVo<Order> queryOrders(OrderDto orderDto);

   Object updateOrders(OrderDto orderDto);

    Object updateOrders2(OrderDto orderDto);

    Integer batchUpdate();

    Integer batchUpdateTest();

    Object query();

    Integer createBatch();
}