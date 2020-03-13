package college.springcloud.io.seata.core.protocol.transaction;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 19:43
 * Version:V1.0
 */
public abstract class AbstractTransactionRequestToTC extends AbstractTransactionRequest {


    protected TCInboundHandler handler;

    /**
     * Sets tc inbound handler.
     *
     * @param handler the handler
     */
    public void setTCInboundHandler(TCInboundHandler handler) {
        this.handler = handler;
    }
}
