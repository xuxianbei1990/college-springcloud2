package state.machine.finance;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 19:49
 * Version:V1.0
 */
public interface StatusEvent {

    int getStatus();

    void executeEvent();
}
