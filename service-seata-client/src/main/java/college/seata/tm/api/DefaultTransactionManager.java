package college.seata.tm.api;

import college.springcloud.io.seata.core.exception.TmTransactionException;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.exception.TransactionExceptionCode;
import college.springcloud.io.seata.core.model.GlobalStatus;
import college.springcloud.io.seata.core.model.TransactionManager;
import college.springcloud.io.seata.core.protocol.transaction.*;
import college.springcloud.io.seata.core.rpc.netty.TmRpcClient;

import java.util.concurrent.TimeoutException;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:51
 * Version:V1.0
 */
public class DefaultTransactionManager implements TransactionManager {
    @Override
    public String begin(String applicationId, String transactionServiceGroup, String name, int timeout) throws TransactionException {
        return null;
    }

    @Override
    public GlobalStatus commit(String xid) throws TransactionException {
        GlobalCommitRequest globalCommit = new GlobalCommitRequest();
        globalCommit.setXid(xid);
        GlobalCommitResponse response = (GlobalCommitResponse) syncCall(globalCommit);
        return response.getGlobalStatus();
    }

    @Override
    public GlobalStatus rollback(String xid) throws TransactionException {
        GlobalRollbackRequest globalRollback = new GlobalRollbackRequest();
        globalRollback.setXid(xid);
        GlobalRollbackResponse response = (GlobalRollbackResponse)syncCall(globalRollback);
        return response.getGlobalStatus();
    }

    private AbstractTransactionResponse syncCall(AbstractTransactionRequest request) throws TransactionException {
        try {
            return (AbstractTransactionResponse) TmRpcClient.getInstance().sendMsgWithResponse(request);
        } catch (TimeoutException toe) {
            throw new TmTransactionException(TransactionExceptionCode.IO, "RPC timeout", toe);
        }
    }
}
