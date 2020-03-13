package college.seata.tm.api;

import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;
import college.springcloud.io.seata.core.model.TransactionManager;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:48
 * Version:V1.0
 */
@Slf4j
public class TransactionManagerHolder {

    private static class SingletonHolder {

        private static TransactionManager INSTANCE = null;

        static {
            try {
                // EnhancedServiceLoader.load(TransactionManager.class); 简单处理
                INSTANCE = new DefaultTransactionManager();
                log.info("TransactionManager Singleton " + INSTANCE);
            } catch (Throwable anyEx) {
                log.error("Failed to load TransactionManager Singleton! ", anyEx);
            }
        }
    }

    public static TransactionManager get() {
        if (SingletonHolder.INSTANCE == null) {
            throw new ShouldNeverHappenException("TransactionManager is NOT ready!");
        }
        return SingletonHolder.INSTANCE;
    }
}
