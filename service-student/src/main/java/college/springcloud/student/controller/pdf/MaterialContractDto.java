package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 辅料合同
 *
 * @author: llq
 * Date: 2021/9/16
 * Time: 16:49
 * Version:V1.0
 */
@Data
public class MaterialContractDto {
    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 供应商名称
     */
    private String vendorName;

    /**
     * 订单编号
     */
    private String poCode;

    /**
     * 客户名称
     */
    private String sellCustomer;

    /**
     * 商品列表
     */
    private List<SkuItem2Dto> tableData;

    /**
     * 总计
     */
    private BigDecimal totalQuantity;
    /**
     * 金额统计
     */
    private BigDecimal totalMoney;
    /**
     * 金额大写
     */
    private String chineseTypeMoney;

    /**
     * 付款方式
     */
    private String parameTer;


    private String parameTer1;

    private String parameTer2;

    private String parameTer3;

    private String parameTer11;

    private String parameTer12;

}
