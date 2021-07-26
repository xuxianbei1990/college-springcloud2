package state.machine.finance;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 20:30
 * Version:V1.0
 */
public class InvoiceStateDelete implements StatusEvent {


    @Override
    public int getStatus() {
        return 1;
    }

    @Override
    public void executeEvent() {
        //这里执行状态的内容
    }
}
