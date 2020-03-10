package college.springcloud.io.seata.core.exception;

import io.seata.core.exception.TransactionExceptionCode;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 11:28
 * Version:V1.0
 */
@Data
public class TransactionException extends Exception  {

    protected TransactionExceptionCode code = TransactionExceptionCode.Unknown;
}
