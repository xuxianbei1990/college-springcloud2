package college.springcloud.order.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单表
 * @author:admin
 */
@Table(name = "t_bbc_order")
@Data
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;
	
    /** 订单号 */
	@Id
    private String forderId;


    /** 支付订单号 */
	@Column(name = "forder_payment_id")
    private String forderPaymentId;

    /** 订单状态：1.待支付  2.待确认 3.待推送 4.待发货 5.待收货 6已收货 7.已完成 8.已取消 */
	@Column(name = "forder_status")
    private Integer forderStatus;

    /** 取消类型：1 额度超限 2缺货 */
	@Column(name = "fcancel_type")
    private Integer fcancelType;

    /** 订单总金额 */
	@Column(name = "forder_amount")
    private Long forderAmount;

    /** 税费 */
	@Column(name = "ftax_amount")
    private Long ftaxAmount;

    /** 运费 */
	@Column(name = "ffreight_amount")
    private Long ffreightAmount;

    /** 创建时间 */
	@Column(name = "fcreate_time")
    private Date fcreateTime;

    /** 修改时间 */
	@Column(name = "fmodify_time")
    private Date fmodifyTime;
}