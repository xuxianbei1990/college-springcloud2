package college.springcloud.io.seata.core.protocol;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 20:04
 * Version:V1.0
 */
@Data
public class MessageFuture {
    private RpcMessage requestMessage;
    private long timeout;
}
