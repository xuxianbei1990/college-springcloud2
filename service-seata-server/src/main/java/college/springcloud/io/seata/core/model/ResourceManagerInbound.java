package college.springcloud.io.seata.core.model;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 9:36
 * Version:V1.0
 * 入口
 */
public interface ResourceManagerInbound {

    BranchStatus branchCommit(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException;
}
