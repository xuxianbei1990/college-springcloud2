package college.springcloud.student.service.template;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报表供应商
 *
 * @author: xuxianbei
 * Date: 2021/12/9
 * Time: 11:17
 * Version:V1.0
 */
@Data
public class PayReportVendorItemVo {

    /**
     * 供应商简称
     */
    private String venAbbName;

    /**
     * 物料类型 0成衣 1物料
     */
    private String materialName;

    /**
     * 入库金额
     */
    private BigDecimal inboundMoney;

    /**
     * 应付金额
     */
    private BigDecimal shouldPay;

    /**
     * 已付金额 预付款
     */
    private BigDecimal preparePay;

    /**
     * 已付金额 结算付款
     */
    private BigDecimal settlementPay;

    /**
     * 已付金额 往期入库金额
     */
    private BigDecimal historyInboundMonney;

    /**
     * 已付金额 本期入库金额
     */
    private BigDecimal currentInboundMoney;

    /**
     * 未提交财务金额（未付） 预付款
     */
    private BigDecimal unCommitPreparePay;

    /**
     * 未提交财务金额（未付） 结算付款
     */
    private BigDecimal unCommitSettlePay;

    /**
     * 已提交财务金额(未付) 预付款
     */
    private BigDecimal commitPreparePay;

    /**
     * 已提交财务金额(未付) 结算付款
     */
    private BigDecimal commitSettlePay;

    /**
     * 已提交核算金额(未付) 预付款
     */
    private BigDecimal approvalPreparePay;

    /**
     * 已提交核算金额(未付) 结算单
     */
    private BigDecimal approvalSettlePay;

    /**
     * 已提交出纳金额（未付） 预付款
     */
    private BigDecimal moneyPreparePay;

    /**
     * 已提交出纳金额（未付） 结算单
     */
    private BigDecimal moneySettlePay;

    /**
     * 已付款进度
     */
    private BigDecimal payProcess;

    /**
     * 已结算进度
     */
    private BigDecimal settleProcess;

}
