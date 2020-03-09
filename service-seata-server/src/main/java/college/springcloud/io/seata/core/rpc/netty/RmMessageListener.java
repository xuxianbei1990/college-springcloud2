package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.rpc.ClientMessageListener;
import college.springcloud.io.seata.core.rpc.ClientMessageSender;
import college.springcloud.io.seata.core.rpc.TransactionMessageHandler;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 15:03
 * Version:V1.0
 */
public class RmMessageListener implements ClientMessageListener {

    private TransactionMessageHandler handler;

    public RmMessageListener(TransactionMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onMessage(RpcMessage request, String serverAddress, ClientMessageSender sender) {

    }
}
