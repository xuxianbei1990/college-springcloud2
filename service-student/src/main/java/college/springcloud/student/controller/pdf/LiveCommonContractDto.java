package college.springcloud.student.controller.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 直播款-普通合同
 *
 * @author: llq
 * Date: 2021/9/16
 * Time: 16:49
 * Version:V1.0
 */
@Data
public class LiveCommonContractDto {
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

    private String  taxRate;


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
