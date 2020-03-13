package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.rpc.RpcContext;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 19:43
 * Version:V1.0
 */
@Data
public class GlobalBeginRequest extends AbstractTransactionRequestToTC {

    private int timeout = 60000;

    private String transactionName;


    ////DefaultCoordinator
    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

    @Override
    public short getTypeCode() {
        return 0;
    }


}
