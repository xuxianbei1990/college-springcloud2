package college.springcloud.common.plug.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import college.springcloud.common.utils.AbstractSerializeUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2020/12/1
 * Time: 11:47
 * Version:V1.0
 */
@Component
public class RootLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements ApplicationContextAware {

    public static Map<String, AbstractLog> strategyMap;
    private static StrategySerialize serialize;
    private final String LOG_OUT_PATTER = "%s \u001b[32m%s\u001b[0m  -||- [%s] [%s] => %s";
    private final String PACKAGE_LINK = ".";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS");

    public RootLogAppender() {
        serialize = new StrategySerialize();
        strategyMap = serialize.getStrategyMap();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (getStrategyMap().isEmpty()) {
            System.out.println(String.format(LOG_OUT_PATTER,
                    dateTimeFormatter.format(LocalDateTime.now())
                    , eventObject.getLevel().toString(),
                    eventObject.getLoggerName().substring(eventObject.getLoggerName().lastIndexOf(PACKAGE_LINK) + 1)
                    , Thread.currentThread().getName(), eventObject.getFormattedMessage()));
            return;

        }
        getStrategyMap().get(eventObject.getLevel().toString()).log(eventObject.getLoggerName(),
                eventObject.getFormattedMessage(), eventObject.getArgumentArray());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansWithAnnotation(Log.class).entrySet().iterator().forEachRemaining(entrySet ->
            strategyMap.put(((AbstractLog) entrySet.getValue()).level().toString(), (AbstractLog) entrySet.getValue())
        );
        //手动保存，用于多类加载的问题
        serialize.serialize();
    }

    public static Map<String, AbstractLog> getStrategyMap() {
        if (strategyMap.isEmpty()) {
            strategyMap = serialize.getStrategyMap();
        }

        return strategyMap;
    }

    private static class StrategySerialize extends AbstractSerializeUtil {
        Map<String, AbstractLog> strategyMap = getStrategyMap();


        public Map<String, AbstractLog> getStrategyMap() {
            Object deserializer = deserializer();
            if (deserializer != null) {
                strategyMap = (Map<String, AbstractLog>) deserializer;
            } else {
                strategyMap = new ConcurrentHashMap<>();
            }
            return strategyMap;
        }

        @Override
        protected void serialize() {
            super.serialize();
        }

        @Override
        public Object getEntity() {
            return strategyMap;
        }
    }
}
