package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 除雪梨生活馆外的成衣合同
 *
 * @author: xuxianbei
 * Date: 2021/9/16
 * Time: 16:59
 * Version:V1.0
 */
@Data
public class XueLiLiveOutContractDto {

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 订单编号
     */
    private String poCode;

    /**
     * 甲方
     */
    private String financialBody;

    /**
     * 乙方
     */
    private String vendorName;

    /**
     * 商品列表
     */
    private List<SkuItem1Dto> tableData;

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
    private String parameter;

    /**
     * 税点
     */
    private String taxRate;

    /**
     * 日期
     */
    private String parameTer1;

    /**
     * 日期
     */
    private String parameTer2;

    /**
     * 金额%
     */
    private String parameTer3;

    /**
     * 人民币
     */
    private String parameTer4;

    private String chinaTer4;

    private String parameTer5;

    private String parameTer6;

    private String parameTer7;

    private String chinaTer6;

    private String parameTer8;

    private String settlementDay;

    private String receivingAddress;

    private String contactPerson;

    private String contactPhone;

    private BigDecimal moreLess1;

    private BigDecimal moreLess2;

    private BigDecimal moreLess3;

    private String accessories;

    /**
     * 总金额
     */
    private String totalSum;
    /**
     * 单件
     */
    private String poNum10;

}
