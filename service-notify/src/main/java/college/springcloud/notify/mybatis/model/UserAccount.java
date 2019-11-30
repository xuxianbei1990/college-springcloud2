package college.springcloud.notify.mybatis.model;

import college.springcloud.common.db.XyNextVersion;
import lombok.Data;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * User: xuxianbei
 * Date: 2019/11/30
 * Time: 10:21
 * Version:V1.0
 */
@Data
@Table(name = "t_bbc_user_account")
public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    private Long fuid;


    /**
     * 可用余额
     */
    @Column(name = "fbalance")
    private Long fbalance;

    /**
     * 信用账户状态：1未激活，2激活，3冻结
     */
    @Column(name = "fcredit_status")
    private Integer fcreditStatus;

    /**
     * 信用额度，额度人工定
     */
    @Column(name = "fcredit_recharge")
    private Long fcreditRecharge;

    /**
     * 信用额度余额
     */
    @Column(name = "fcredit_balance")
    private Long fcreditBalance;

    /**
     * 信用支付金额
     */
    @Column(name = "fcredit_pay_amount")
    private Long fcreditPayAmount;

    /**
     * 充值金额
     */
    @Column(name = "frecharge")
    private Long frecharge;

    /**
     * 提现金额
     */
    @Column(name = "fwithdraw")
    private Long fwithdraw;

    /**
     * 提现冻结金额
     */
    @Column(name = "ffreeze_withdraw")
    private Long ffreezeWithdraw;

    /**
     * 支付中冻结金额
     */
    @Column(name = "ffreeze_pay")
    private Long ffreezePay;

    /**
     * 操作备注
     */
    @Column(name = "foperate_remark")
    private String foperateRemark;

    /**
     * 创建时间
     */
    @Column(name = "fcreate_time")
    private Date fcreateTime;

    /**
     * 修改时间
     */
    @Column(name = "fmodify_time")
    @Version(nextVersion = XyNextVersion.class)
    private Date fmodifyTime;
}
