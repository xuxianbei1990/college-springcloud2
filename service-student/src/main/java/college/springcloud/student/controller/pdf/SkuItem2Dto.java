package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2021/9/15
 * Time: 16:54
 * Version:V1.0
 */
@Data
public class SkuItem2Dto {

    /**
     * sku
     */
    private String inventoryCode;

    /**
     * 商品名称
     */
    private String inventoryName;

    /**
     * 规格型号
     */
    private String specName;

    /**
     * 尺寸
     */
    private String specificationsRemark;


    /**
     * 件数
     */
    private Integer quantity;

    /**
     * 含税单件/元
     */
    private BigDecimal taxUnitPrice;

    /**
     * 采购税率
     */
    private BigDecimal taxRate;

    /**
     * 税额/元
     */
    private String taxMoney;

    /**
     * 价税合计/元
     */
    private BigDecimal includedTaxMoney;

    /**
     * 合同开始日期
     */
    private String conStartDate;

    /**
     * 合同截止日期
     */
    private String conEndDate;
}
