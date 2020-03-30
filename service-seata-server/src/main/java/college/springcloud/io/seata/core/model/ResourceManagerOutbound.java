package college.springcloud.io.seata.core.model;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * @author: xuxianbei
 * Date: 2020/3/26
 * Time: 11:33
 * Version:V1.0
 */
public interface ResourceManagerOutbound {
    void branchReport(BranchType branchType, String xid, long branchId, BranchStatus status, String applicationData) throws TransactionException;
}
