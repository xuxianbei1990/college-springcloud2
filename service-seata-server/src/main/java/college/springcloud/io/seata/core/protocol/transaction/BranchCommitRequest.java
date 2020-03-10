package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.protocol.MessageType;
import college.springcloud.io.seata.core.rpc.RpcContext;

/**
 * 分支提交请求
 *
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:43
 * Version:V1.0
 */
public class BranchCommitRequest extends AbstractBranchEndRequest  {

    @Override
    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_COMMIT;
    }

    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this);
    }
}
