package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * 这个和SessionLifeCycleListener 不是一样吗？
 * @author: xuxianbei
 * Date: 2020/3/16
 * Time: 17:16
 * Version:V1.0
 */
public interface SessionLifecycle {

    /**
     * Begin.
     *
     * @throws TransactionException the transaction exception
     */
    void begin() throws TransactionException;
}
