package college.springcloud.io.seata.core.protocol.transaction;

import college.springcloud.io.seata.core.exception.TransactionExceptionCode;
import college.springcloud.io.seata.core.protocol.AbstractResultMessage;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 9:48
 * Version:V1.0
 */
@Data
public abstract class AbstractTransactionResponse extends AbstractResultMessage {

    private TransactionExceptionCode transactionExceptionCode = TransactionExceptionCode.Unknown;
}
