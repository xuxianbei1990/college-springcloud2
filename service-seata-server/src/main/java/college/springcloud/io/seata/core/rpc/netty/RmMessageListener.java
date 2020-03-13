package college.springcloud.io.seata.core.rpc.netty;

import college.springcloud.io.seata.core.protocol.RpcMessage;
import college.springcloud.io.seata.core.protocol.transaction.BranchCommitRequest;
import college.springcloud.io.seata.core.protocol.transaction.BranchCommitResponse;
import college.springcloud.io.seata.core.rpc.ClientMessageListener;
import college.springcloud.io.seata.core.rpc.ClientMessageSender;
import college.springcloud.io.seata.core.rpc.TransactionMessageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息接收
 *
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 15:03
 * Version:V1.0
 */
@Slf4j
public class RmMessageListener implements ClientMessageListener {

    private TransactionMessageHandler handler;

    public RmMessageListener(TransactionMessageHandler handler) {
        this.handler = handler;
    }

    /**
     * 分支提交
     * 分支回滚
     * undolog删除
     *
     * @param request       the msg id
     * @param serverAddress the server address
     * @param sender        the sender
     */
    @Override
    public void onMessage(RpcMessage request, String serverAddress, ClientMessageSender sender) {
        Object msg = request.getBody();
        if (log.isInfoEnabled()) {
            log.info("onMessage:" + msg);
        }

        //收到分支提交请求只是删除了undo日志？
        if (msg instanceof BranchCommitRequest) {
            handleBranchCommit(request, serverAddress, (BranchCommitRequest) msg, sender);
        }
    }


    private void handleBranchCommit(RpcMessage request, String serverAddress, BranchCommitRequest branchCommitRequest,
                                    ClientMessageSender sender) {
        BranchCommitResponse resultMessage;
        resultMessage = (BranchCommitResponse) handler.onRequest(branchCommitRequest, null);
        sender.sendResponse(request, serverAddress, resultMessage);
    }
}
