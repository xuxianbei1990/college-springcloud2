package college.springcloud.io.seata.server.store;

import college.springcloud.io.seata.core.exception.AbstractExceptionHandler;
import college.springcloud.io.seata.core.exception.StoreException;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.exception.TransactionExceptionCode;
import college.springcloud.io.seata.core.protocol.transaction.*;
import college.springcloud.io.seata.core.rpc.RpcContext;
import lombok.extern.slf4j.Slf4j;

/** 处理消息
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:24
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractTCInboundHandler extends AbstractExceptionHandler implements TCInboundHandler {

    @Override
    public GlobalBeginResponse handle(GlobalBeginRequest request, RpcContext rpcContext) {
        GlobalBeginResponse response = new GlobalBeginResponse();
        exceptionHandleTemplate(new AbstractExceptionHandler.AbstractCallback<GlobalBeginRequest, GlobalBeginResponse>() {
            @Override
            public void execute(GlobalBeginRequest request, GlobalBeginResponse response) throws TransactionException {
                try {
                    doGlobalBegin(request, response, rpcContext);
                } catch (Exception e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore,
                            String.format("begin global request failed. xid=%s, msg=%s", response.getXid(), e.getMessage()),
                            e);
                }
            }
        }, request, response);
        return response;
    }

    protected abstract void doGlobalBegin(GlobalBeginRequest request, GlobalBeginResponse response,
                                          RpcContext rpcContext) throws TransactionException;



    @Override
    public GlobalCommitResponse handle(GlobalCommitRequest request, final RpcContext rpcContext) {
        GlobalCommitResponse response = new GlobalCommitResponse();
        exceptionHandleTemplate(new AbstractCallback<GlobalCommitRequest, GlobalCommitResponse>() {
            @Override
            public void execute(GlobalCommitRequest request, GlobalCommitResponse response)
                    throws TransactionException {
                try {
                    doGlobalCommit(request, response, rpcContext);
                } catch (StoreException e) {
                    throw new TransactionException(TransactionExceptionCode.FailedStore,
                            String.format("global commit request failed. xid=%s, msg=%s", request.getXid(), e.getMessage()),
                            e);
                }
            }
        }, request, response);
        return response;
    }

    /**
     * Do global commit.
     *
     * @param request    the request
     * @param response   the response
     * @param rpcContext the rpc context
     * @throws TransactionException the transaction exception
     */
    protected abstract void doGlobalCommit(GlobalCommitRequest request, GlobalCommitResponse response,
                                           RpcContext rpcContext) throws TransactionException;
}
