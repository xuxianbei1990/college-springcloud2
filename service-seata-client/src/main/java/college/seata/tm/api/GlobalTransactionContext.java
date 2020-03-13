package college.seata.tm.api;

import college.springcloud.io.seata.core.context.RootContext;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:40
 * Version:V1.0
 */
public class GlobalTransactionContext {

    private GlobalTransactionContext() {
    }

    public static GlobalTransaction getCurrentOrCreate() {
        GlobalTransaction tx = getCurrent();
        if (tx == null) {
            return createNew();
        }
        return tx;
    }

    private static GlobalTransaction createNew() {
        GlobalTransaction tx = new DefaultGlobalTransaction();
        return tx;
    }

    private static GlobalTransaction getCurrent() {
        String xid = RootContext.getXID();
        if (xid == null) {
            return null;
        }
        return null;
//        return new DefaultGlobalTransaction(xid, GlobalStatus.Begin, GlobalTransactionRole.Participant);
    }
}
