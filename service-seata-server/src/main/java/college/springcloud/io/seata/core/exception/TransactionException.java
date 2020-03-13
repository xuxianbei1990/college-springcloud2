package college.springcloud.io.seata.core.exception;

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

    public TransactionException(TransactionExceptionCode code) {
        this.code = code;
    }

    public TransactionException(TransactionExceptionCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(TransactionExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(TransactionExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
