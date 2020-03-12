package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.model.BranchType;
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

    /**
     * The Branch id.
     */
    protected long branchId;

    protected BranchType branchType = BranchType.AT;

    /**
     * 数据库？
     * The Resource id.
     */
    protected String resourceId;

    protected String applicationData;
}
