package college.springcloud.log.alarm;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import college.springcloud.log.util.tool.PushUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 16:00
 * Version:V1.0
 */
public class AlarmLog extends AppenderBase<ILoggingEvent> {
    private final Map<String, Integer> alarmCache = new ConcurrentHashMap<>();
    private final int DEFAULT_ALARM_COUNT = 1;
    private final int MAX_ALARM_COUNT = 4;
    private final String EMPTY = "";
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executorService.shutdown()));
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        //增加系统错误的通知
        if (Level.TRACE.equals(eventObject.getLevel())) {
            //去除系统中的线程影响
            String key = eventObject.getFormattedMessage().replaceAll(eventObject.getThreadName(), EMPTY);
            Integer count = alarmCache.merge(key, DEFAULT_ALARM_COUNT, Integer::sum);
            if (count == MAX_ALARM_COUNT) {
                IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
                String msg;
                if (throwableProxy != null) {
                    msg = "**" + eventObject.getFormattedMessage() + "**\n###### " + throwableProxy.getMessage() + "\n" +
                            Arrays.stream(throwableProxy.getStackTraceElementProxyArray()).map(String::valueOf).collect(Collectors.joining("\n- "));
                } else {
                    msg = "**" + eventObject.getFormattedMessage() + "**";
                }
                executorService.execute(() -> PushUtil.getInstance().send(PushUtil.PushType.DINGDING, EMPTY, msg));
                alarmCache.put(key, Integer.MIN_VALUE);
            }
        }
    }
}
