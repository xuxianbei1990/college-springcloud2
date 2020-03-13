package college.springcloud.io.seata.core.rpc;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:49
 * Version:V1.0
 */
@Data
public class RpcContext {

    private String transactionServiceGroup;

    private String applicationId;
}
