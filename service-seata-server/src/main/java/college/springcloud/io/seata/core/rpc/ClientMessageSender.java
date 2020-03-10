package college.springcloud.io.seata.core.rpc;


import college.springcloud.io.seata.core.protocol.RpcMessage;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 20:18
 * Version:V1.0
 */
public interface ClientMessageSender {

    void sendResponse(RpcMessage request, String serverAddress, Object msg);
}
