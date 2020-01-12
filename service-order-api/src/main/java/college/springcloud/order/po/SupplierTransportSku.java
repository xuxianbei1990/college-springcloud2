package college.springcloud.order.po;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2020/1/9
 * Time: 15:53
 * Version:V1.0
 */
@Table(name = "t_bbc_supplier_transport_sku")
public class SupplierTransportSku implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增id */
    @Id
    private Long ftransportSkuId;


    /** 发货单号 */
    @Column(name = "ftransport_order_id")
    private String ftransportOrderId;

    /** 批次号 */
    @Column(name = "fbatch_id")
    private String fbatchId;

    /** 批次包装规格id */
    @Column(name = "fbatch_package_id")
    private Long fbatchPackageId;

    /** 批次包装规格值 */
    @Column(name = "fbatch_package_num")
    private Long fbatchPackageNum;

    /** sku编号 */
    @Column(name = "fsku_code")
    private String fskuCode;

    /** sku名称 */
    @Column(name = "fsku_name")
    private String fskuName;

    /** sku数量 */
    @Column(name = "fsku_num")
    private Integer fskuNum;

    /** sku单价 */
    @Column(name = "fsku_price")
    private Long fskuPrice;

    /** 优惠金额 */
    @Column(name = "fdiscount_amount")
    private Long fdiscountAmount;

    /** 创建时间 */
    @Column(name = "fcreate_time")
    private Date fcreateTime;

    /** 修改时间 */
    @Column(name = "fmodify_time")
    private Date fmodifyTime;


    public void setFtransportSkuId(Long ftransportSkuId) {
        this.ftransportSkuId = ftransportSkuId;
    }

    public Long getFtransportSkuId() {
        return this.ftransportSkuId;
    }
    public void setFtransportOrderId(String ftransportOrderId) {
        this.ftransportOrderId = ftransportOrderId;
    }

    public String getFtransportOrderId() {
        return this.ftransportOrderId;
    }
    public void setFbatchId(String fbatchId) {
        this.fbatchId = fbatchId;
    }

    public String getFbatchId() {
        return this.fbatchId;
    }
    public void setFbatchPackageId(Long fbatchPackageId) {
        this.fbatchPackageId = fbatchPackageId;
    }

    public Long getFbatchPackageId() {
        return this.fbatchPackageId;
    }

    public Long getFbatchPackageNum() {
        return fbatchPackageNum;
    }

    public void setFbatchPackageNum(Long fbatchPackageNum) {
        this.fbatchPackageNum = fbatchPackageNum;
    }

    public void setFskuCode(String fskuCode) {
        this.fskuCode = fskuCode;
    }

    public String getFskuCode() {
        return this.fskuCode;
    }
    public void setFskuName(String fskuName) {
        this.fskuName = fskuName;
    }

    public String getFskuName() {
        return this.fskuName;
    }
    public void setFskuNum(Integer fskuNum) {
        this.fskuNum = fskuNum;
    }

    public Integer getFskuNum() {
        return this.fskuNum;
    }
    public void setFskuPrice(Long fskuPrice) {
        this.fskuPrice = fskuPrice;
    }

    public Long getFskuPrice() {
        return this.fskuPrice;
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

    public Long getFdiscountAmount() {
        return fdiscountAmount;
    }

    public void setFdiscountAmount(Long fdiscountAmount) {
        this.fdiscountAmount = fdiscountAmount;
    }
}
