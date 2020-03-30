package college.seata.rm.datasource;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/26
 * Time: 11:25
 * Version:V1.0
 */
@Data
public class ConnectionContext {

    private String xid;
    private Long branchId;

    public boolean inGlobalTransaction() {
        return xid != null;
    }

    public boolean isBranchRegistered() {
        return branchId != null;
    }

    public void reset() {}
}
