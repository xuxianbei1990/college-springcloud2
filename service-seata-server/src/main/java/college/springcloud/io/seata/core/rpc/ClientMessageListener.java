package college.springcloud.io.seata.core.rpc;

import college.springcloud.io.seata.core.protocol.RpcMessage;

/** 客户端用于消息接收顶层接口
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 15:00
 * Version:V1.0
 */
public interface ClientMessageListener {

    /**
     * On message.
     *  为什么是这三个参数，一个消息体，一个地址，一个发送？
     * @param request       the msg id
     * @param serverAddress the server address
     * @param sender        the sender
     */
    void onMessage(RpcMessage request, String serverAddress, ClientMessageSender sender);
}
