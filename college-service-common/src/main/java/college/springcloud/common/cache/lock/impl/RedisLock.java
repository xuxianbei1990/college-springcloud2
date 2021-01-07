package college.springcloud.common.cache.lock.impl;

import college.springcloud.common.cache.lock.CacheLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁
 *
 * @author penglu
 * @version 1.0.0
 * @date 2019-08-17
 * @copyright 本内容仅限于浙江云贸科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class RedisLock implements CacheLock {

    //通过Lua脚本删除key,确保原子操作
    private static final String DEL_LUA_SCRIPT = "if redis.call('GET', KEYS[1]) == ARGV[1] then return redis.call('DEL', KEYS[1]) else return 0 end";

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean tryLock(String key, String value, long expiring, TimeUnit timeUnit) {
//        Boolean result = redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
//                redisConnection.set(SafeEncoder.encode(key), SafeEncoder.encode(value), Expiration.seconds(expiring),
//                        RedisStringCommands.SetOption.SET_IF_ABSENT));
//        return result.booleanValue();
        return true;
    }

    @Override
    public boolean releaseLock(String key, String value) {
//        Boolean result = redisTemplate.execute((RedisCallback<Boolean>) redisConnection ->
//                redisConnection.eval(DEL_LUA_SCRIPT.getBytes(), ReturnType.BOOLEAN, 1,
//                        SafeEncoder.encode(key), SafeEncoder.encode(value)));
//        return result.booleanValue();
        return true;
    }

    @Override
    public boolean tryLockTimes(String key, String value, int times, long expiring) {
        times = times <= 0 ? 1 : times;
        times = times > 10 ? 10 : times;
        for (int i = 0; i < times; i++) {
            boolean getLock = this.tryLock(key, value, expiring, TimeUnit.MILLISECONDS);
            if (getLock) {
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(0, 100) + 1);
            } catch (InterruptedException e) {
//                log.error("tryLockTimes sleep error...", e);
            }
        }
        return false;
    }

}
