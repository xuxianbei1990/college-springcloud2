package college.springcloud.io.seata.server.session;


import college.springcloud.io.seata.core.exception.BranchTransactionException;
import college.springcloud.io.seata.core.exception.GlobalTransactionException;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.exception.TransactionExceptionCode;
import college.springcloud.io.seata.server.store.SessionStorable;
import college.springcloud.io.seata.server.store.TransactionStoreManager;
import college.springcloud.io.seata.server.store.TransactionStoreManager.LogOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 18:22
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractSessionManager implements SessionManager, SessionLifecycleListener {

    /**
     * The Transaction store manager.
     */
    protected TransactionStoreManager transactionStoreManager;

    /**
     * The Name.
     */
    protected String name;

    @Override
    public void onBegin(GlobalSession globalSession) throws TransactionException {
        addGlobalSession(globalSession);
    }

    @Override
    public void addGlobalSession(GlobalSession session) throws TransactionException {
        if (log.isDebugEnabled()) {
            log.debug("MANAGER[" + name + "] SESSION[" + session + "] " + TransactionStoreManager.LogOperation.GLOBAL_ADD);
        }
        writeSession(TransactionStoreManager.LogOperation.GLOBAL_ADD, session);
    }

    private void writeSession(TransactionStoreManager.LogOperation logOperation, SessionStorable sessionStorable) throws TransactionException {
        if (!transactionStoreManager.writeSession(logOperation, sessionStorable)) {
            if (LogOperation.GLOBAL_ADD.equals(logOperation)) {
                throw new GlobalTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to store global session");
            } else if (LogOperation.GLOBAL_UPDATE.equals(logOperation)) {
                throw new GlobalTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to update global session");
            } else if (LogOperation.GLOBAL_REMOVE.equals(logOperation)) {
                throw new GlobalTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to remove global session");
            } else if (LogOperation.BRANCH_ADD.equals(logOperation)) {
                throw new BranchTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to store branch session");
            } else if (LogOperation.BRANCH_UPDATE.equals(logOperation)) {
                throw new BranchTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to update branch session");
            } else if (LogOperation.BRANCH_REMOVE.equals(logOperation)) {
                throw new BranchTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Fail to remove branch session");
            } else {
                throw new BranchTransactionException(TransactionExceptionCode.FailedWriteSession,
                        "Unknown LogOperation:" + logOperation.name());
            }
        }
    }
}
