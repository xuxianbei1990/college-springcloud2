package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.model.BranchStatus;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 9:41
 * Version:V1.0
 * 处理分支结束响应
 */
@Data
public abstract class AbstractBranchEndResponse extends AbstractTransactionResponse {
    /**
     * The Xid.
     */
    protected String xid;

    /**
     * The Branch id.
     */
    protected long branchId;
    /**
     * The Branch status.
     */
    protected BranchStatus branchStatus;
}
