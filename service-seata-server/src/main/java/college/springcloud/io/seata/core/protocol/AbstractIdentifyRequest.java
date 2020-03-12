package college.springcloud.io.seata.core.protocol;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 11:43
 * Version:V1.0
 */
public abstract class AbstractIdentifyRequest extends AbstractMessage {


    /**
     * The Application id.
     */
    protected String applicationId;

    /**
     * The Transaction service group.
     */
    protected String transactionServiceGroup;

    public AbstractIdentifyRequest(String applicationId, String transactionServiceGroup) {
        this.applicationId = applicationId;
        this.transactionServiceGroup = transactionServiceGroup;
    }
}
