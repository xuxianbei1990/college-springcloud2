package college.springcloud.io.seata.core.protocol.transaction;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 20:38
 * Version:V1.0
 */
@Data
public abstract class AbstractBranchEndRequest extends AbstractTransactionRequestToRM {
    /**
     * The Xid.
     */
    protected String xid;
}
