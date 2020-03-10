package college.springcloud.io.seata.core.exception;

import college.springcloud.io.seata.core.protocol.ResultCode;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionRequest;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionResponse;

/**
 * 这里是不是主要触发回滚的，所以干脆把这些类放到异常里处理
 *
 * 主要功能
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 11:29
 * Version:V1.0
 */
public class AbstractExceptionHandler {

    public abstract static class AbstractCallback<T extends AbstractTransactionRequest, S extends AbstractTransactionResponse>
            implements Callback<T, S> {

        @Override
        public void onSuccess(T request, S response) {
            response.setResultCode(ResultCode.Success);
        }

        @Override
        public void onTransactionException(T request, S response,
                                           TransactionException tex) {
            response.setTransactionExceptionCode(tex.getCode());
            response.setResultCode(ResultCode.Failed);
            response.setMsg("TransactionException[" + tex.getMessage() + "]");
        }

        @Override
        public void onException(T request, S response, Exception rex) {
            response.setResultCode(ResultCode.Failed);
            response.setMsg("RuntimeException[" + rex.getMessage() + "]");
        }
    }

    public interface Callback<T extends AbstractTransactionRequest, S extends AbstractTransactionResponse> {
        /**
         * Execute.
         *
         * @param request  the request
         * @param response the response
         * @throws TransactionException the transaction exception
         */
        void execute(T request, S response) throws TransactionException;

        /**
         * on Success
         *
         * @param request
         * @param response
         */
        void onSuccess(T request, S response);

        /**
         * onTransactionException
         *
         * @param request
         * @param response
         * @param exception
         */
        void onTransactionException(T request, S response, TransactionException exception);

        /**
         * on other exception
         *
         * @param request
         * @param response
         * @param exception
         */
        void onException(T request, S response, Exception exception);

    }
}
