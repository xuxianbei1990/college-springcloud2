package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.rpc.RpcContext;

/**  TC入口处理
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 19:44
 * Version:V1.0
 */
public interface TCInboundHandler {


    GlobalBeginResponse handle(GlobalBeginRequest globalBegin, RpcContext rpcContext);

    /**
     * Handle global commit response.
     *
     * @param globalCommit the global commit
     * @param rpcContext   the rpc context
     * @return the global commit response
     */
    GlobalCommitResponse handle(GlobalCommitRequest globalCommit, RpcContext rpcContext);

    /**
     * Handle global rollback response.
     *
     * @param globalRollback the global rollback
     * @param rpcContext     the rpc context
     * @return the global rollback response
     */
    GlobalRollbackResponse handle(GlobalRollbackRequest globalRollback, RpcContext rpcContext);
}
