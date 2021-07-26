package state.machine.order;

/**
 * @author: xuxianbei
 * Date: 2021/2/26
 * Time: 11:00
 * Version:V1.0
 */
public enum OrderStatus {
    // 待支付，待发货，待收货，订单结束
    WAIT_PAYMENT, WAIT_DELIVER, WAIT_RECEIVE, FINISH;
}
