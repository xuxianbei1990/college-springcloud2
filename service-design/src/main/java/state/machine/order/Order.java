package state.machine.order;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/2/26
 * Time: 10:56
 * Version:V1.0
 */
@Data
public class Order {
    private int id;
    private OrderStatus status;
}
