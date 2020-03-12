package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.protocol.MessageType;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:53
 * Version:V1.0
 * 处理支付提交响应
 */
public class BranchCommitResponse extends AbstractBranchEndResponse {

    public short getTypeCode() {
        return MessageType.TYPE_BRANCH_COMMIT_RESULT;
    }
}
