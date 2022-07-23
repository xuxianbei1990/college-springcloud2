package state.machine.finance;

import lombok.Getter;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 19:59
 * Version:V1.0
 */
@Getter
public enum InvoiceHeaderEnum {
    INVOICE_STATUS_ELEVNE(11, "待打款", () -> {
    }),
    INVOICE_STATUS_TWELVE(12, "已开票");

    InvoiceHeaderEnum(Integer code, String desc) {

    }

    InvoiceHeaderEnum(Integer code, String desc, Event event) {
        this.code = code;
        this.desc = desc;
        this.event = event;
    }

    private Integer code;
    private String desc;
    private Event event;

}
