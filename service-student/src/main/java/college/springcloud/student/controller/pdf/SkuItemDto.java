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
public class SkuItemDto {

    /**
     * 商品款号
     */
    private String productCode;

    /**
     * 商品名称
     */
    private String inventoryName;

    /**
     * 颜色
     */
    private String color;

    /**
     * 尺码
     */
    private Integer size;

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
    private BigDecimal taxMoney;

    /**
     * 价税合计/元
     */
    private BigDecimal includedTaxMoney;

    /**
     * 发货日期
     */
    private String conEndDate;
}
