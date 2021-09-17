package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 初礼合同
 *
 * @author: llq
 * Date: 2021/9/16
 * Time: 16:49
 * Version:V1.0
 */
@Data
public class FirstGiveContractDto {
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
     * 财务主体
     */
    private String financialBody;

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
    private String parameTer;


    private String parameTer1;


    private String parameTer2;


    private String parameTer3;


    private String parameTer4;


    private String parameTer5;


    private String parameTer6;


    private String parameTer7;


    private String parameTer8;

    /**
     * 生效后几天内
     */
    private String  day1;
    /**
     * 生效后几天内
     */
    private String  day2;

    private BigDecimal bargain;
    private BigDecimal bargainRatio;
    private String syBargain;

    private String  taxRate1;

    private String settlementDay;

    /**
     * 地址
     * */
    private String receivingAddress;

    /**
     *联系人
     * */
    private String contactPerson;

    /**
     *联系电话
     * */
    private String contactPhone;

    private BigDecimal moreLess1;

    private BigDecimal moreLess2;

    private BigDecimal moreLess3;


    private BigDecimal  accessories;
    /**
     * 总金额
     */
    private BigDecimal totalSum;
    /**
     * 单件
     */
    private BigDecimal poNum10;
}
