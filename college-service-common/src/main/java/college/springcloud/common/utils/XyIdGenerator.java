package college.springcloud.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Title 分布式ID生成器
 * @Package com.xingyun.bbc.common.redis
 * @Description: 分布式ID生成器
 * ID: orderType + bizType + 时间戳 + platform + 机器位 + 随机数
 * @Author Tito
 * @Date 2019/9/20 14:29
 * @Company © 版权所有 深圳市天行云供应链有限公司
 * @Version v1.0
 */
@Component
public class XyIdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XyIdGenerator.class);


    /**
     * 默认随机数长度
     */
    private static final int DEFAULT_RANDOM_LENGTH = 4;


    /**
     * 生成ID
     *
     * @param orderType 订单类型
     * @return
     */
    public static String generateId(String orderType) {
        return joinId(orderType, null, null, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * 生成ID
     *
     * @param orderType 订单类型
     * @param bizType   业务类型
     * @return
     */
    public static String generateId(String orderType, String bizType) {
        return joinId(orderType, bizType, null, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * 生成ID
     *
     * @param orderType 订单类型
     * @param randomLen 随机数长度
     * @return
     */
    public static String generateId(String orderType, int randomLen) {
        return joinId(orderType, null, null, randomLen);
    }

    /**
     * 生成ID
     *
     * @param orderType 订单类型
     * @param bizType   业务类型
     * @param randomLen 随机数长度
     * @return
     */
    public static String generateId(String orderType, String bizType, int randomLen) {
        return joinId(orderType, bizType, null, randomLen);
    }

    /**
     * @param orderType 订单类型
     * @param bizType   业务类型
     * @param platform  下单平台
     * @return
     */
    public static String generateId(String orderType, String bizType, String platform) {
        return joinId(orderType, bizType, platform, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * @param orderType 订单类型
     * @param bizType   业务类型
     * @param platform  下单平台
     * @param randomLen 随机数长度
     * @return
     */
    public static String generateId(String orderType, String bizType, String platform, int randomLen) {
        return joinId(orderType, bizType, platform, randomLen);
    }

    /**
     * @param orderType 订单类型
     * @param platform  下单平台
     * @return
     */
    public static String generateIdWithPlatform(String orderType, String platform) {
        return joinId(orderType, null, platform, DEFAULT_RANDOM_LENGTH);
    }

    /**
     * @param orderType 订单类型
     * @param platform  下单平台
     * @param randomLen 随机数长度
     * @return
     */
    public static String generateIdWithPlatform(String orderType, String platform, int randomLen) {
        return joinId(orderType, null, platform, randomLen);
    }


    /**
     * 拼接订单id
     *
     * @param orderType 订单类型
     * @param bizType   业务类型, 可为空
     * @param platform  下单平台, 可为空
     * @param randomLen 随机数长度
     * @return 订单号
     */
    private static String joinId(String orderType, String bizType, String platform, int randomLen) {
        long currentTime = System.currentTimeMillis(); // 当前时间，毫秒
        StringBuilder buffer = new StringBuilder(orderType); // StringBuffer线程安全 不存在 所以改为StringBuilder
        if (bizType != null && (bizType = bizType.trim()).length() != 0) {
            buffer.append(bizType);
        }
        buffer.append(currentTime - 1_000_000_000_000L); // 去除第一位
        if (platform != null && (platform = platform.trim()).length() != 0) {
            buffer.append(platform);
        }
        String randomStr = String.valueOf(ThreadLocalRandom.current().nextInt((int) Math.pow(10, randomLen))); // ThreadLocalRandom线程安全
        // 往左填充0
        for (int i = randomStr.length(); i < randomLen; i++) {
            buffer.append('0');
        }
        buffer.append(randomStr);
        String orderId = buffer.toString();
        return orderId;
    }
}

