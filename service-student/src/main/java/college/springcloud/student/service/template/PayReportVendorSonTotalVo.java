package college.springcloud.student.service.template;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 供应商报表小计
 *
 * @author: xuxianbei
 * Date: 2021/12/21
 * Time: 20:25
 * Version:V1.0
 */
@Data
public class PayReportVendorSonTotalVo {
    /**
     * 供应商简称
     */
    private String sonTotalName = "小计";

    /**
     * 已付金额 预付金额
     */
    private BigDecimal preparePay = BigDecimal.ZERO;

    /**
     * 已付金额 结算金额
     */
    private BigDecimal settlementPay = BigDecimal.ZERO;

    /**
     * 已付金额 往期入库金额
     */
    private BigDecimal historyInboundMonney = BigDecimal.ZERO;

    /**
     * 已付金额 本期入库金额
     */
    private BigDecimal currentInboundMoney = BigDecimal.ZERO;

    /**
     * 未提交财务金额（未付） 预付款
     */
    private BigDecimal unCommitPreparePay = BigDecimal.ZERO;

    /**
     * 未提交财务金额（未付） 结算付款
     */
    private BigDecimal unCommitSettlePay = BigDecimal.ZERO;

    /**
     * 已提交财务金额(未付) 预付款
     */
    private BigDecimal commitPreparePay = BigDecimal.ZERO;

    /**
     * 已提交财务金额(未付) 结算付款
     */
    private BigDecimal commitSettlePay = BigDecimal.ZERO;

    /**
     * 已提交核算金额(未付) 预付款
     */
    private BigDecimal approvalPreparePay = BigDecimal.ZERO;

    /**
     * 已提交核算金额(未付) 结算单
     */
    private BigDecimal approvalSettlePay = BigDecimal.ZERO;

    /**
     * 已提交出纳金额（未付） 预付款
     */
    private BigDecimal moneyPreparePay = BigDecimal.ZERO;

    /**
     * 已提交出纳金额（未付） 结算单
     */
    private BigDecimal moneySettlePay = BigDecimal.ZERO;
}
