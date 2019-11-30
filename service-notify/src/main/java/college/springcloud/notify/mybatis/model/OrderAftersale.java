package college.springcloud.notify.mybatis.model;

import college.springcloud.common.db.XyNextVersion;
import lombok.Data;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * User: xuxianbei
 * Date: 2019/11/29
 * Time: 20:22
 * Version:V1.0
 */
@Data
@Table(name = "t_bbc_order_aftersale")
public class OrderAftersale implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 售后单号 */
    @Id
    private String forderAftersaleId;


    /** 用户id */
    @Column(name = "fuid")
    private Long fuid;

    /** 处理对象类型 1供应商 2平台 */
    @Column(name = "fdeal_type")
    private Integer fdealType;

    /** 订单号 */
    @Column(name = "forder_id")
    private String forderId;

    /** 供应商采购单号 */
    @Column(name = "fsupplier_order_id")
    private String fsupplierOrderId;

    /** 发货单号 */
    @Column(name = "ftransport_order_id")
    private String ftransportOrderId;

    /** 批次号 */
    @Column(name = "fbatch_id")
    private String fbatchId;


    /** 售后类型 1 退款 2 退款退货 */
    @Column(name = "faftersale_type")
    private Integer faftersaleType;

    /** SKU编码 */
    @Column(name = "fsku_code")
    private String fskuCode;

    /** 售后数量 */
    @Column(name = "faftersale_num")
    private Integer faftersaleNum;

    /** 售后原因类型 1客户申请 2供应商无法发货 3供应商漏发 4供应商延期发货 5供应商商品发错 6商品质量问题 7商品运输破损 */
    @Column(name = "faftersale_reason")
    private Integer faftersaleReason;

    /** 供应商id */
    @Column(name = "fsupplier_id")
    private Long fsupplierId;

    /** sku单价--下单时的单价 */
    @Column(name = "funit_price")
    private Long funitPrice;


    /** 创建时间 */
    @Column(name = "fcreate_time")
    private Date fcreateTime;

    /** 修改时间 */
    @Column(name = "fmodify_time")
    @Version(nextVersion = XyNextVersion.class)
    private Date fmodifyTime;
}
