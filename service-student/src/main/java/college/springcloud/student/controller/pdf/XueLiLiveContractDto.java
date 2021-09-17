package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 雪梨生活馆
 * @author: xuxianbei
 * Date: 2021/9/16
 * Time: 20:07
 * Version:V1.0
 */
@Data
public class XueLiLiveContractDto {

    /**
     * 品牌
     */
    private String brandName;

    /**
     * 甲方
     */
    private String financialBody;

    /**
     * 乙方
     */
    private String vendorName;

    /**
     * 订单编号
     */
    private String poCode;

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
     * 付款方式
     */
    private String parameTer1;

    /**
     * 付款方式
     */
    private String parameTer11;

    /**
     * 付款方式
     */
    private String parameTer3;

    /**
     * 付款方式
     */
    private String parameTer2;

    /**
     * 付款方式
     */
    private String bargain;

    /**
     * 付款方式
     */
    private String parameTer5;

    /**
     * 付款方式
     */
    private String barMoney;

    /**
     * 付款方式
     */
    private String settlementDay;

    /**
     * 付款方式
     */
    private String parameTer14;

    /**
     * 付款方式
     */
    private String parameTer15;

    /**
     * 付款方式
     */
    private String parameTer12;

    /**
     * 付款方式
     */
    private String parameTer13;


    private String receivingAddress;

    private String contactPerson;

    private String contactPhone;
}
