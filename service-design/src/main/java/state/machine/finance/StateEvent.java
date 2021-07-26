package state.machine.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 20:07
 * Version:V1.0
 */
@Getter
@AllArgsConstructor
public class StateEvent {

    private InvoiceHeaderEnum invoiceHeaderEnum;

    private StatusEvent event;


}
