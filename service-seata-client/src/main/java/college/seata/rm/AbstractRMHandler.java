package college.seata.rm;

import college.springcloud.io.seata.core.exception.AbstractExceptionHandler.AbstractCallback;
import college.springcloud.io.seata.core.exception.AbstractExceptionHandler.Callback;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManager;
import college.springcloud.io.seata.core.protocol.AbstractMessage;
import college.springcloud.io.seata.core.protocol.AbstractResultMessage;
import college.springcloud.io.seata.core.protocol.transaction.*;
import college.springcloud.io.seata.core.rpc.RpcContext;
import college.springcloud.io.seata.core.rpc.TransactionMessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 事件处理转发？
 * 接收request, 同时处理对应消息
 * 我猜测设计者的想法：我做好设计到后期如果需要拆分的话，可以快速拆分。
 *
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 16:05
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractRMHandler implements TransactionMessageHandler, RMInboundHandler {

    @Override
    public AbstractResultMessage onRequest(AbstractMessage request, RpcContext context) {
        if (!(request instanceof AbstractTransactionRequestToRM)) {
            throw new IllegalArgumentException();
        }

        AbstractTransactionRequestToRM transactionRequest = (AbstractTransactionRequestToRM) request;
        transactionRequest.setRMInboundMessageHandler(this);
        return transactionRequest.handle(context);
    }

    @Override
    public BranchCommitResponse handle(BranchCommitRequest request) {
        BranchCommitResponse response = new BranchCommitResponse();
        //这种写法我还是第一次看到
        exceptionHandleTemplate(new AbstractCallback<BranchCommitRequest, BranchCommitResponse>() {

            @Override
            public void execute(BranchCommitRequest request, BranchCommitResponse response)
                    throws TransactionException {
                doBranchCommit(request, response);
            }
        }, request, response);
        return response;
    }


    public void exceptionHandleTemplate(Callback callback, AbstractTransactionRequest request,
                                        AbstractTransactionResponse response) {
        try {
            callback.execute(request, response);
            callback.onSuccess(request, response);
        } catch (TransactionException tex) {
            log.error("Catch TransactionException while do RPC, request: {}", request, tex);
            callback.onTransactionException(request, response, tex);
        } catch (RuntimeException rex) {
            log.error("Catch RuntimeException while do RPC, request: {}", request, rex);
            callback.onException(request, response, rex);
        }
    }

    protected void doBranchCommit(BranchCommitRequest request, BranchCommitResponse response)
            throws TransactionException {
        String xid = request.getXid();
        long branchId = request.getBranchId();
        //这个是数据库？
        String resourceId = request.getResourceId();
        String applicationData = request.getApplicationData();
        log.info("Branch committing: " + xid + " " + branchId + " " + resourceId + " " + applicationData);
        //这里调用的是RMHandleAT 的 getResourceManager() 最终调用 DataSourceManager
        BranchStatus status = getResourceManager().branchCommit(request.getBranchType(), xid, branchId, resourceId,
                applicationData);
        response.setXid(xid);
        response.setBranchId(branchId);
        response.setBranchStatus(status);
    }

    protected abstract ResourceManager getResourceManager();

    public abstract BranchType getBranchType();
}
