package college.springcloud.io.seata.core.protocol.transaction;

/** 为什么定义这么多分层，其实就是每个分层处理自己要处理的东西
 *
 * 这个类其实只处理输入和输出的，对应的就是request 和response
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:51
 * Version:V1.0
 */
public abstract class AbstractTransactionRequestToRM extends AbstractTransactionRequest {

    /**
     * The Handler.
     */
    protected RMInboundHandler handler;

    public void setRMInboundMessageHandler(RMInboundHandler handler) {
        this.handler = handler;
    }
}
