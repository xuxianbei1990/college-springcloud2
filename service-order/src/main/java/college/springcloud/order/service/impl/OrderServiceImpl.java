package college.springcloud.order.service.impl;


import college.springcloud.common.model.vo.PageVo;
import college.springcloud.common.query.Criteria;
import college.springcloud.common.service.BaseService;
import college.springcloud.order.dto.OrderDto;
import college.springcloud.order.mapper.OrderMapper;
import college.springcloud.order.po.Order;
import college.springcloud.order.service.OrderService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author admin
 */
@Service
public class OrderServiceImpl extends BaseService<Order> implements OrderService {

    @Resource
    OrderMapper orderMapper;

    @Override
    public PageVo<Order> queryOrders(OrderDto orderDto) {
        Criteria<Order, Object> criteria = Criteria.of(Order.class).page(orderDto.getCurrentPage(), orderDto.getPageSize());
        //挺适合业务代码。
//        criteria.andEqualTo(Order::getForderId, orderDto.getForderId());
        List<Order> resultList = queryByCriteria(criteria);
        Integer count = countByCriteria(criteria);
        return new PageVo(count, orderDto.getCurrentPage(), orderDto.getPageSize(),
                resultList);
    }

    /**
     * 测试乐观锁
     *
     * @param orderDto
     * @return
     */
    @Override
    public Object updateOrders(OrderDto orderDto) {
        //测试结果乐观锁实际是初始化时候，通过添加sql  AND fmodify_time = #{fmodifyTime}</where> 实现的 sqlHelper
        //他的nextVersion其实不是用来作为版本判断，是用来更新的
        Order order = queryById(orderDto.getForderId());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //证明了如果自己写的sql不会添加乐观锁的
//        orderMapper.updateOrderStatus(order);
        order.setFtaxAmount(0L);
        return updateNotNull(order);
    }

    @Override
    public Object updateOrders2(OrderDto orderDto) {
        //测试结果乐观锁实际是初始化时候，通过添加sql  AND fmodify_time = #{fmodifyTime}</where> 实现的
        Order order = queryById(orderDto.getForderId());
        order.setForderId(orderDto.getForderId());
        order.setFtaxAmount(10L);
        return updateNotNull(order);
    }

    // mysql 5.7.27 i7-9700  3.0GHZ 16G内存
//    //case when 66860 10000 记录
////    foreach  372784
    @Override
    public Integer batchUpdate() {
        Long l = System.currentTimeMillis();
        try {
            Order order = new Order();
            order.setForderStatus(4);
            List<Order> orderList = orderMapper.select(order);
            orderList.stream().forEach(order1 -> order1.setFcancelType(0));
            return orderMapper.updateBatchNotNullBySinglePrimaryKey(orderList);
        } finally {
            System.out.println(System.currentTimeMillis() - l);
        }
    }

    //测试foreach  372784
    @Override
    public Integer batchUpdateTest() {
        Long l = System.currentTimeMillis();
        try {
            Order order = new Order();
            order.setForderStatus(4);
            List<Order> orderList = orderMapper.select(order);
            orderList.stream().forEach(order1 -> order1.setFcancelType(1));
            return orderMapper.batchUpdate(orderList);
        } finally {
            System.out.println(System.currentTimeMillis() - l);
        }
    }

    @Override
    public Object query() {
        Criteria<Order, Object> criteria = Criteria.of(Order.class);
        criteria.andLike(Order::getForderId, "%XS57112392226100");
        List<Order> orderList = queryByCriteria(criteria);
        System.out.println(DateFormatUtils.format(orderList.get(0).getFmodifyTime(), DateUtils.PATTERN_RFC1123));
        return orderList;
    }

    @Override
    public Integer createBatch() {
        Order order = new Order();
        order.setForderStatus(3);
        List<Order> orderList = orderMapper.select(order);
        orderList.stream().forEach(t -> t.setForderId(t.getForderId() + "X"));
        Integer result = orderMapper.insertList(orderList);
        return result;
    }
}