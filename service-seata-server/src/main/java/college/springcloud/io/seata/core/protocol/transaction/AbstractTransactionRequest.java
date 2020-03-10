package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.protocol.AbstractMessage;
import college.springcloud.io.seata.core.rpc.RpcContext;

/**
 *
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:46
 * Version:V1.0
 */
public abstract class AbstractTransactionRequest extends AbstractMessage {

    /**
     * Handle abstract transaction response.
     *
     * @param rpcContext the rpc context
     * @return the abstract transaction response
     */
    public abstract AbstractTransactionResponse handle(RpcContext rpcContext);

}
