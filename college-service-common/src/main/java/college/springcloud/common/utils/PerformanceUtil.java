package college.springcloud.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/1/21
 * Time: 15:30
 * Version:V1.0
 */
public class PerformanceUtil {

    private static Long allTime;
    private static String key;
    private static Map<String, Long> timeLongs = new HashMap();

    public static void allEnd() {
        long cost = System.currentTimeMillis() - allTime;
        System.out.println(cost);
        timeLongs.entrySet().forEach(t -> System.out.println(t.getKey() + "->" + BigDecimal.valueOf(t.getValue())
                .divide(BigDecimal.valueOf(cost), 2, RoundingMode.HALF_UP)));
    }

    public static void allStart() {
        allTime = System.currentTimeMillis();
    }

    public static void start() {
        start("");
    }

    public static void start(String key) {
        Long startTime = System.currentTimeMillis();
        PerformanceUtil.key = timeLongs.size() + ":" + key;
        timeLongs.put(PerformanceUtil.key, startTime);
    }

    private void end() {
        timeLongs.put(PerformanceUtil.key, System.currentTimeMillis() - timeLongs.get(PerformanceUtil.key));
        System.out.println(timeLongs.get(timeLongs.size() - 1));
    }
}
