package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 一件代发合同
 *
 * @author: xuxianbei
 * Date: 2021/9/15
 * Time: 16:49
 * Version:V1.0
 */
@Data
public class OneForDistributionContractDto {

    /**
     * 供应商名称
     */
    private String vendorName;

    /**
     * 订单编号
     */
    private String poCode;

    /**
     * 财务主体
     */
    private String financialBody;

    /**
     * 商品列表
     */
    private List<SkuItemDto> tableData;

    /**
     * 总计
     */
    private Integer totalQuantity;

    /**
     * 金额大写
     */
    private String chineseTypeMoney;

    /**
     * 辅料套数
     */
    private Integer accessories;

    /**
     * 总金额
     */
    private BigDecimal totalSum;

    /**
     * 单件
     */
    private BigDecimal poNum10;
}
