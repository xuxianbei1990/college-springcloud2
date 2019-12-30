package college.springcloud.stream.producter;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author nick
 * @version 1.0.0
 * @date 2019-12-23
 * @copyright 本内容仅限于深圳市天行云供应链有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Accessors(chain = true)
public class MsgPushDto implements Serializable {

    private static final long serialVersionUID = 1491619211122663366L;

    /**
     * 通知类型1.站内通知 2.消息推送 3.系统通知
     *

     */
    @NotNull(message = "通知类型不能为空")
    private Integer pushType;


    /**
     * 发送时间 1.站内通知 2.消息推送必须
     */
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;

    /**
     * 1.站内通知 2.消息推送的模板Id
     */
    private Long templateId;

    /**
     *======================》下面属性是系统通知需要封装《=====================
     */

    /**
     * 系统通知的替换变量实体
     */
    private MsgTemplateVariableDto msgTemplateVariable;


    /**
     * 系统通知的模板类型模板类型
     * <p>
     * 1发货单发货  2注册成功 3修改绑定手机号 4优惠券到账 5优惠券将要过期
     * 6认证成功 7发货提醒 8售后消息 9售后工单提醒 10调整工单消息 11补充工单消息
     * 12采购单即将逾期消息 13采购单已逾期消息 14商品失效  15库存预警 16批次采购
     * 17批次销售 18批次冻结 19批次解冻 20结算单生成 21结算单已完成 22结算单新增付款单
     *
     */
    private Integer systemTemplateType;

    /**
     * 系统通知发送主体 1.平台会员 2.供应商
     *

     */
    private Integer subjectType;

    /**
     * 系统通知发送主体Id(会员/供应商)
     */
    private Long subjectId;
}
