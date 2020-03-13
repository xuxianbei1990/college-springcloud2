package college.seata.tm.api;

import college.springcloud.io.seata.core.exception.TmTransactionException;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.exception.TransactionExceptionCode;
import college.springcloud.io.seata.core.model.GlobalStatus;
import college.springcloud.io.seata.core.model.TransactionManager;
import college.springcloud.io.seata.core.protocol.ResultCode;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionRequest;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionResponse;
import college.springcloud.io.seata.core.protocol.transaction.GlobalBeginRequest;
import college.springcloud.io.seata.core.protocol.transaction.GlobalBeginResponse;
import college.springcloud.io.seata.core.rpc.netty.TmRpcClient;

import java.util.concurrent.TimeoutException;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:45
 * Version:V1.0
 */
public class DefaultGlobalTransaction implements GlobalTransaction {

    private TransactionManager transactionManager;

    private String xid;

    private GlobalStatus status;

    private GlobalTransactionRole role;

    DefaultGlobalTransaction() {
        this(null, GlobalStatus.UnKnown, GlobalTransactionRole.Launcher);
    }

    DefaultGlobalTransaction(String xid, GlobalStatus status, GlobalTransactionRole role) {
        this.transactionManager = TransactionManagerHolder.get();
        this.xid = xid;
        this.status = status;
        this.role = role;
    }

    @Override
    public void begin(int timeout, String name) throws TransactionException {
        GlobalBeginRequest request = new GlobalBeginRequest();
        request.setTransactionName(name);
        request.setTimeout(timeout);
        GlobalBeginResponse response = (GlobalBeginResponse)syncCall(request);
        if (response.getResultCode() == ResultCode.Failed) {
            throw new TmTransactionException(TransactionExceptionCode.BeginFailed, response.getMsg());
        }
    }

    private AbstractTransactionResponse syncCall(AbstractTransactionRequest request) throws TransactionException {
        try {
            return (AbstractTransactionResponse) TmRpcClient.getInstance().sendMsgWithResponse(request);
        } catch (TimeoutException toe) {
            throw new RuntimeException("RPC timeout", toe);
        }
    }
}
