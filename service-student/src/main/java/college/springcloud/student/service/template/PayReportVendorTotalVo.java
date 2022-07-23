package college.springcloud.student.service.template;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 供应商报表总计
 *
 * @author: xuxianbei
 * Date: 2021/12/21
 * Time: 20:26
 * Version:V1.0
 */
@Data
public class PayReportVendorTotalVo {

    /**
     * 供应商简称
     */
    private String totalName = "总计";

    /**
     * 入库金额
     */
    private BigDecimal inboundMoney = BigDecimal.ZERO;

    /**
     * 应付金额
     */
    private BigDecimal shouldPay = BigDecimal.ZERO;

    /**
     * 已付金额
     */
    private BigDecimal payed = BigDecimal.ZERO;

    /**
     * 已付金额
     */
    private BigDecimal rocordPayed = BigDecimal.ZERO;

    /**
     * 未提交财务金额（未付）
     */
    private BigDecimal unCommitPay = BigDecimal.ZERO;

    /**
     * 已提交财务金额(未付)
     */
    private BigDecimal commitPay = BigDecimal.ZERO;

    /**
     * 已提交核算金额(未付)
     */
    private BigDecimal approvalPay = BigDecimal.ZERO;

    /**
     * 已提交出纳金额（未付）
     */
    private BigDecimal moneyPay = BigDecimal.ZERO;

    /**
     * 已付款进度
     */
    private BigDecimal payProcess = BigDecimal.ZERO;

    /**
     * 已结算进度
     */
    private BigDecimal settleProcess = BigDecimal.ZERO;
}
