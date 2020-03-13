package college.seata.tm.api.transaction;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:55
 * Version:V1.0
 */
public interface TransactionHook {

    /**
     * before tx begin
     */
    void beforeBegin();

    /**
     * after tx begin
     */
    void afterBegin();

    /**
     * before tx commit
     */
    void beforeCommit();

    /**
     * after tx commit
     */
    void afterCommit();

    /**
     * before tx rollback
     */
    void beforeRollback();

    /**
     * after tx rollback
     */
    void afterRollback();

    /**
     * after tx all Completed
     */
    void afterCompletion();
}
