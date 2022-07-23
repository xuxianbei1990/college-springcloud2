package state.machine.finance;

import lombok.Getter;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 20:34
 * Version:V1.0
 */
@Getter
public class StateFactory {

    public static StateFactory getInstance(){
        return new StateFactory();
    }

    private InvoiceStateDelete invoiceStateDelete;
}
