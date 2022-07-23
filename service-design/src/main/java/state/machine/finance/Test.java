package state.machine.finance;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 20:02
 * Version:V1.0
 */
public class Test {
    public static void main(String[] args) {
        StateEvent stateEvent = new StateEvent(
                InvoiceHeaderEnum.INVOICE_STATUS_ELEVNE,
        new StatusEvent() {

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public void executeEvent() {

            }
        });

        StateFactory.getInstance().getInvoiceStateDelete();
    }
}
