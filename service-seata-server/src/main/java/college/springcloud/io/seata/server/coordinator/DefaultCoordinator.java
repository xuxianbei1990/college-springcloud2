package college.springcloud.io.seata.server.coordinator;

import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import college.springcloud.io.seata.core.protocol.AbstractMessage;
import college.springcloud.io.seata.core.protocol.AbstractResultMessage;
import college.springcloud.io.seata.core.protocol.transaction.*;
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
    public BranchStatus branchRollback(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
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
        //这里源码就做了两件事，一个统计，一个把数据插入到global_table, 然后返回了一个xid，就这样了
        response.setXid(core.begin(rpcContext.getApplicationId(), rpcContext.getTransactionServiceGroup(),
                request.getTransactionName(), request.getTimeout()));
    }

    @Override
    protected void doGlobalCommit(GlobalCommitRequest request, GlobalCommitResponse response, RpcContext rpcContext) throws TransactionException {

    }


    @Override
    public GlobalRollbackResponse handle(GlobalRollbackRequest globalRollback, RpcContext rpcContext) {
        return null;
    }
}
