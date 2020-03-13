package college.springcloud.io.seata.core.rpc;


import college.springcloud.io.seata.core.protocol.RpcMessage;

import java.util.concurrent.TimeoutException;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 20:18
 * Version:V1.0
 */
public interface ClientMessageSender {

    void sendResponse(RpcMessage request, String serverAddress, Object msg);

    Object sendMsgWithResponse(Object msg, long timeout) throws TimeoutException;

//    Object sendMsgWithResponse(String serverAddress, Object msg, long timeout) throws TimeoutException;

    /**
     * Send msg with response object.
     *
     * @param msg the msg
     * @return the object
     * @throws TimeoutException the timeout exception
     */
    Object sendMsgWithResponse(Object msg) throws TimeoutException;
}
