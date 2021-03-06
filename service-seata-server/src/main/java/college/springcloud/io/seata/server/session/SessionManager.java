package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * @author: xuxianbei
 * Date: 2020/3/16
 * Time: 17:12
 * Version:V1.0
 */
public interface SessionManager extends SessionLifecycleListener {

    /**
     * Add global session.
     *
     * @param session the session
     * @throws TransactionException the transaction exception
     */
    void addGlobalSession(GlobalSession session) throws TransactionException;
}
