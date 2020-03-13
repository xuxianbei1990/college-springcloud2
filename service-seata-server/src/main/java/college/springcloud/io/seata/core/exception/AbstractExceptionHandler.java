package college.springcloud.io.seata.core.exception;

import college.springcloud.io.seata.core.protocol.ResultCode;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionRequest;
import college.springcloud.io.seata.core.protocol.transaction.AbstractTransactionResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 这里是不是主要触发回滚的，所以干脆把这些类放到异常里处理
 *
 * 主要功能
 * @author: xuxianbei
 * Date: 2020/3/10
 * Time: 11:29
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractExceptionHandler {

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

    /**
     * Exception handle template.
     *
     * @param callback the callback
     * @param request  the request
     * @param response the response
     */
    public void exceptionHandleTemplate(Callback callback, AbstractTransactionRequest request,
                                        AbstractTransactionResponse response) {
        try {
            callback.execute(request, response);
            callback.onSuccess(request, response);
        } catch (TransactionException tex) {
            log.error("Catch TransactionException while do RPC, request: {}", request, tex);
            callback.onTransactionException(request, response, tex);
        } catch (RuntimeException rex) {
            log.error("Catch RuntimeException while do RPC, request: {}", request, rex);
            callback.onException(request, response, rex);
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
