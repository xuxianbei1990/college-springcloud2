package college.seata.tm.api;

import college.seata.tm.api.transaction.TransactionHook;
import college.seata.tm.api.transaction.TransactionHookManager;
import college.seata.tm.api.transaction.TransactionInfo;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;
import college.springcloud.io.seata.core.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:15
 * Version:V1.0
 */
@Slf4j
public class TransactionalTemplate {

    public Object execute(TransactionalExecutor business) throws Throwable {
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        // 1.1 get transactionInfo
        TransactionInfo txInfo = business.getTransactionInfo();

        if (txInfo == null) {
            throw new ShouldNeverHappenException("transactionInfo does not exist");
        }

        try {

            // 2. begin transaction  发送一个请求
            beginTransaction(txInfo, tx);

            Object rs = null;
            try {

                // 这里有意思了，也就说这里实际是没有添加事务的？因为开始事务只是发送了一个请求
                //至少在客户端只是发送了一个这样的请求
                rs = business.execute();

            } catch (Throwable ex) {

                // 3.the needed business exception to rollback.
//                completeTransactionAfterThrowing(txInfo,tx,ex);
                throw ex;
            }

            // 4. everything is fine, commit.
//            commitTransaction(tx);

            return rs;
        } finally {
            //5. clear
//            triggerAfterCompletion();
//            cleanUp();
        }
    }

    private void beginTransaction(TransactionInfo txInfo, GlobalTransaction tx) throws TransactionalExecutor.ExecutionException {
        try {
            //这一步只有测试使用
//            triggerBeforeBegin();
            //发送请求
            tx.begin(txInfo.getTimeOut(), txInfo.getName());
        } catch (TransactionException txe) {
            throw new TransactionalExecutor.ExecutionException(tx, txe,
                    TransactionalExecutor.Code.BeginFailure);

        }
    }

    private void triggerBeforeBegin() {
        for (TransactionHook hook : getCurrentHooks()) {
            try {
                hook.beforeBegin();
            } catch (Exception e) {
                log.error("Failed execute beforeBegin in hook {}",e.getMessage(),e);
            }
        }
    }

    private List<TransactionHook> getCurrentHooks() {
        return TransactionHookManager.getHooks();
    }
}
