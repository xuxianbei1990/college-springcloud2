package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 18:18
 * Version:V1.0
 */
public interface SessionLifecycleListener {

    void onBegin(GlobalSession globalSession) throws TransactionException;
}
