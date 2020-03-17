package college.springcloud.io.seata.core.model;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:49
 * Version:V1.0
 */
public interface TransactionManager {

    /**
     * Begin a new global transaction.
     *
     * @param applicationId           ID of the application who begins this transaction.
     * @param transactionServiceGroup ID of the transaction service group.
     * @param name                    Give a name to the global transaction.
     * @param timeout                 Timeout of the global transaction.
     * @return XID of the global transaction
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown
     * out.
     */
    String begin(String applicationId, String transactionServiceGroup, String name, int timeout)
            throws TransactionException;


    /**
     * Global commit.
     *
     * @param xid XID of the global transaction.
     * @return Status of the global transaction after committing.
     * @throws TransactionException Any exception that fails this will be wrapped with TransactionException and thrown
     * out.
     */
    GlobalStatus commit(String xid) throws TransactionException;
}
