package college.springcloud.io.seata.core.rpc;

import college.springcloud.io.seata.core.protocol.AbstractMessage;
import college.springcloud.io.seata.core.protocol.AbstractResultMessage;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 16:00
 * Version:V1.0
 */
public interface TransactionMessageHandler {


    AbstractResultMessage onRequest(AbstractMessage request, RpcContext context);
}
