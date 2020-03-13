package college.springcloud.io.seata.server.coordinator;

import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import college.springcloud.io.seata.core.protocol.AbstractMessage;
import college.springcloud.io.seata.core.protocol.AbstractResultMessage;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionRequestToTC;
import college.springcloud.io.seata.core.protocol.transaction.GlobalBeginRequest;
import college.springcloud.io.seata.core.protocol.transaction.GlobalBeginResponse;
import college.springcloud.io.seata.core.rpc.RpcContext;
import college.springcloud.io.seata.core.rpc.ServerMessageSender;
import college.springcloud.io.seata.core.rpc.TransactionMessageHandler;
import college.springcloud.io.seata.server.store.AbstractTCInboundHandler;

/**
 * 这个默认协调器  制定规则
 * 比如GlobalBeginRequest
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:03
 * Version:V1.0
 */
public class DefaultCoordinator extends AbstractTCInboundHandler implements ResourceManagerInbound, TransactionMessageHandler {

    private ServerMessageSender messageSender;

    private Core core = CoreFactory.get();

    public DefaultCoordinator(ServerMessageSender messageSender) {
        this.messageSender = messageSender;
        core.setResourceManagerInbound(this);
    }

    @Override
    public BranchStatus branchCommit(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        return null;
    }

    @Override
    public AbstractResultMessage onRequest(AbstractMessage request, RpcContext context) {
        if (!(request instanceof AbstractTransactionRequestToTC)) {
            throw new IllegalArgumentException();
        }
        AbstractTransactionRequestToTC transactionRequest = (AbstractTransactionRequestToTC) request;
        transactionRequest.setTCInboundHandler(this);
        return transactionRequest.handle(context);
    }

    @Override
    protected void doGlobalBegin(GlobalBeginRequest request, GlobalBeginResponse response, RpcContext rpcContext)
            throws TransactionException {
        response.setXid(core.begin(rpcContext.getApplicationId(), rpcContext.getTransactionServiceGroup(),
                request.getTransactionName(), request.getTimeout()));
    }


}
