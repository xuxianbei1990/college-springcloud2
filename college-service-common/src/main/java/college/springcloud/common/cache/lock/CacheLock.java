package college.springcloud.common.cache.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author xuxianbei
 * @version 1.0.0
 * @date 2019-08-17
 * @copyright
 */
public interface CacheLock {

    /**
     * 加锁
     *
     * @param key
     * @param value
     * @param expiring 单位秒
     * @return
     */
    boolean tryLock(String key, String value, long expiring, TimeUnit timeUnit);

    /**
     * 解锁
     *
     * @param key
     * @param value
     * @return
     */
    boolean releaseLock(String key, String value);

    /**
     * 加锁尝试几次
     *
     * @param key      锁key
     * @param value
     * @param times    尝试次数
     * @param expiring 单位秒
     * @return
     */
    boolean tryLockTimes(String key, String value, int times, long expiring);

}
