package college.springcloud.io.seata.core.protocol.transaction;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:53
 * Version:V1.0
 */
public interface RMInboundHandler {

    /**
     * Handle branch commit response.
     *
     * @param request the request
     * @return the branch commit response
     */
    BranchCommitResponse handle(BranchCommitRequest request);
}
