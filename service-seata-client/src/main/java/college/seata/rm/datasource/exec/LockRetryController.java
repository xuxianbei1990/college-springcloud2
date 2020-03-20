package college.seata.rm.datasource.exec;

/**
 * @author: xuxianbei
 * Date: 2020/3/20
 * Time: 16:44
 * Version:V1.0
 */
public class LockRetryController {

    private int lockRetryInternal = 10;
    private int lockRetryTimes = 30;

    public LockRetryController() {
    }

    /**
     * Sleep.
     *
     * @param e the e
     * @throws LockWaitTimeoutException the lock wait timeout exception
     */
    public void sleep(Exception e) throws LockWaitTimeoutException {
        if (--lockRetryTimes < 0) {
            throw new LockWaitTimeoutException("Global lock wait timeout", e);
        }

        try {
            Thread.sleep(lockRetryInternal);
        } catch (InterruptedException ignore) {
        }
    }
}
