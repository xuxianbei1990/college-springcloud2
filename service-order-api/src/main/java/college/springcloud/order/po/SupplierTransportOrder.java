package college.springcloud.order.po;

import college.springcloud.common.db.XyNextVersion;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2020/1/9
 * Time: 15:58
 * Version:V1.0
 */
//@Table(name = "t_bbc_supplier_transport_order")
public class SupplierTransportOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 发货单号 */
    @Id
    private String ftransportOrderId;


    /** 供应商采购单号 */
    @Column(name = "fsupplier_order_id")
    private String fsupplierOrderId;

    /** 订单号 */
    @Column(name = "forder_id")
    private String forderId;

    /** 发货单状态 1待发货 2待收货 3已签收 4已完成 */
    @Column(name = "fstaus")
    private Integer fstaus;

    /** 支付订单号 */
    @Column(name = "forder_payment_id")
    private String forderPaymentId;

    /** 物流单号 */
    @Column(name = "forder_logistics_no")
    private String forderLogisticsNo;

    /** 物流公司id */
    @Column(name = "fshipping_company_id")
    private Long fshippingCompanyId;

    /** 物流公司编号 */
    @Column(name = "fshipping_code")
    private String fshippingCode;

    /** 物流公司名称 */
    @Column(name = "fshipping_name")
    private String fshippingName;

    /** 发货时间 */
    @Column(name = "fdelivery_time")
    private Date fdeliveryTime;

    /** 备注 */
    @Column(name = "fremark")
    private String fremark;

    /** 创建时间 */
    @Column(name = "fcreate_time")
    private Date fcreateTime;

    /** 修改时间 */
    @Column(name = "fmodify_time")
    @Version(nextVersion = XyNextVersion.class)
    private Date fmodifyTime;

    public void setFtransportOrderId(String ftransportOrderId) {
        this.ftransportOrderId = ftransportOrderId;
    }

    public String getFtransportOrderId() {
        return this.ftransportOrderId;
    }
    public void setFsupplierOrderId(String fsupplierOrderId) {
        this.fsupplierOrderId = fsupplierOrderId;
    }

    public String getFsupplierOrderId() {
        return this.fsupplierOrderId;
    }
    public void setForderId(String forderId) {
        this.forderId = forderId;
    }

    public String getForderId() {
        return this.forderId;
    }
    public void setFstaus(Integer fstaus) {
        this.fstaus = fstaus;
    }

    public Integer getFstaus() {
        return this.fstaus;
    }
    public void setForderPaymentId(String forderPaymentId) {
        this.forderPaymentId = forderPaymentId;
    }

    public String getForderPaymentId() {
        return this.forderPaymentId;
    }
    public void setForderLogisticsNo(String forderLogisticsNo) {
        this.forderLogisticsNo = forderLogisticsNo;
    }

    public String getForderLogisticsNo() {
        return this.forderLogisticsNo;
    }
    public void setFshippingCompanyId(Long fshippingCompanyId) {
        this.fshippingCompanyId = fshippingCompanyId;
    }

    public Long getFshippingCompanyId() {
        return this.fshippingCompanyId;
    }
    public void setFshippingCode(String fshippingCode) {
        this.fshippingCode = fshippingCode;
    }

    public String getFshippingCode() {
        return this.fshippingCode;
    }
    public void setFshippingName(String fshippingName) {
        this.fshippingName = fshippingName;
    }

    public String getFshippingName() {
        return this.fshippingName;
    }
    public void setFdeliveryTime(Date fdeliveryTime) {
        this.fdeliveryTime = fdeliveryTime;
    }

    public Date getFdeliveryTime() {
        return this.fdeliveryTime;
    }
    public void setFremark(String fremark) {
        this.fremark = fremark;
    }

    public String getFremark() {
        return this.fremark;
    }
    public void setFcreateTime(Date fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    public Date getFcreateTime() {
        return this.fcreateTime;
    }
    public void setFmodifyTime(Date fmodifyTime) {
        this.fmodifyTime = fmodifyTime;
    }

    public Date getFmodifyTime() {
        return this.fmodifyTime;
    }
}
