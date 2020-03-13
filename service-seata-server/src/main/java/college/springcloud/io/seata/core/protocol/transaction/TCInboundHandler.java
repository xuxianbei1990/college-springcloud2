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
}
