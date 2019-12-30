package college.springcloud.stream.producter;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nick
 * @version 1.0.0
 * @date 2019-12-23
 * @copyright 本内容仅限于深圳市天行云供应链有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Accessors(chain = true)
public class MsgTemplateVariableDto implements Serializable {

    private static final long serialVersionUID = -2058072280624113643L;

    public static final Map<String, String> templateVariableMap ;

    static {
        Map<String, String> variableMap = new HashMap<>();
        variableMap.put("${采购订单号}", "fsupplierOrderId");
        variableMap.put("${售后订单号}", "forderAftersaleId");
        variableMap.put("${售后工单号}", "fsupplierWorkOrder");
        variableMap.put("${调整工单号}", "fsupplierWorkOrder");
        variableMap.put("${补充工单号}", "fsupplierWorkOrder");
        variableMap.put("${SKU国际条码}", "finternationalCode");
        variableMap.put("${批次号}", "fbatchId");
        variableMap.put("${结算单号}", "fsettleId");
        variableMap.put("${回款单号}", "fsupplierPaymentId");
        variableMap.put("${物流名称}", "fshippingName");
        variableMap.put("${运单号}", "forderLogisticsNo");
        variableMap.put("${新手机号}", "fmobile");
        variableMap.put("${优惠券金额}", "fdeductionValue");
        variableMap.put("${优惠券到期日期}", "fvalidityEnd");
        variableMap.put("${认证类型}", "foperateMethod");
        templateVariableMap = Collections.unmodifiableMap(variableMap);
    }

    /** 采购订单号 */
    private String fsupplierOrderId;

    /** 售后订单号 */
    private String forderAftersaleId;

    /** 供应商售后工单|调整工单|补偿工单 */
    private String fsupplierWorkOrder;

    /** SKU国际条码 */
    private String finternationalCode;

    /** 批次号 */
    private String fbatchId;

    /** 结算单号 */
    private String fsettleId;

    /** 回款单号 */
    private String fsupplierPaymentId;

    /** 物流名称 */
    private String fshippingName;

    /** 运单号 */
    private String forderLogisticsNo;

    /** 新手机号 */
    private String fmobile;

    /** 优惠券金额 */
    private String fdeductionValue;

    /** 优惠券到期日期 */
    private String fvalidityEnd;

    /** 认证类型 */
    private String foperateMethod;

    /** 发货单号 */
    private String ftransportOrderId;
}
