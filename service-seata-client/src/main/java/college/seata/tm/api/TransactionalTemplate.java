package college.seata.tm.api;

import college.seata.tm.api.transaction.TransactionHook;
import college.seata.tm.api.transaction.TransactionHookManager;
import college.seata.tm.api.transaction.TransactionInfo;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;
import college.springcloud.io.seata.core.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 一个事务开始总入口
 *
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
                //至少在客户端只是发送了一个这样的请求 ;而服务端只是在表里记录了下。然后做了一个统计

                //这个方法就是执行原有的流程、例如远程调用某个方法
                rs = business.execute();

            } catch (Throwable ex) {

                // 3.the needed business exception to rollback.  反正这里是回滚，这里等等
                completeTransactionAfterThrowing(txInfo, tx, ex);
                throw ex;
            }

            // 4. everything is fine, commit.  这里提交事务
            commitTransaction(tx);

            return rs;
        } finally {
            //5. clear
//            triggerAfterCompletion();
//            cleanUp();
        }
    }

    private void commitTransaction(GlobalTransaction tx) throws TransactionalExecutor.ExecutionException {
        try {
            tx.commit();
        } catch (TransactionException txe) {
            // 4.1 Failed to commit
            throw new TransactionalExecutor.ExecutionException(tx, txe,
                    TransactionalExecutor.Code.CommitFailure);
        }
    }

    private void completeTransactionAfterThrowing(TransactionInfo txInfo, GlobalTransaction tx, Throwable ex) throws TransactionalExecutor.ExecutionException {
        //roll back  明天做个试验看下
        if (txInfo != null && txInfo.rollbackOn(ex)) {
            try {
                rollbackTransaction(tx, ex);
            } catch (TransactionException txe) {
                // Failed to rollback
                throw new TransactionalExecutor.ExecutionException(tx, txe,
                        TransactionalExecutor.Code.RollbackFailure, ex);
            }
        } else {
//            // not roll back on this exception, so commit
            commitTransaction(tx);
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

    private void rollbackTransaction(GlobalTransaction tx, Throwable ex) throws TransactionException, TransactionalExecutor.ExecutionException {
        tx.rollback();
        // 3.1 Successfully rolled back
        throw new TransactionalExecutor.ExecutionException(tx, TransactionalExecutor.Code.RollbackDone, ex);
    }

    private List<TransactionHook> getCurrentHooks() {
        return TransactionHookManager.getHooks();
    }
}
