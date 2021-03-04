package college.springcloud.log.plug.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import college.springcloud.log.util.start.AbstractSerializeUtil;
import college.springcloud.log.util.start.ApplicationContextUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2021/3/2
 * Time: 14:00
 * Version:V1.0
 */
public class RootLogBackLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements ApplicationContextAware {

    public static Map<String, AbstractLog> strategyMap;
    private static StrategySerialize serialize;

    public RootLogBackLogAppender() {
        init();
    }

    protected void init() {
        serialize = new StrategySerialize();
        strategyMap = serialize.getStrategyMap();
    }

    public static Map<String, AbstractLog> getStrategyMap() {
        if (strategyMap.isEmpty()) {
            strategyMap = serialize.getStrategyMap();
        }
        return strategyMap;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        //默认没启动时候控制台输出
        if (getStrategyMap().isEmpty()) {
            return;
        }

        Object[] paramArray;
        if (eventObject.getThrowableProxy() != null && eventObject.getThrowableProxy() instanceof ThrowableProxy) {
            ThrowableProxy throwableProxy = (ThrowableProxy) eventObject.getThrowableProxy();
            paramArray = new Object[]{eventObject.getArgumentArray(), throwableProxy.getThrowable()};
        } else {
            paramArray = eventObject.getArgumentArray();
        }
        //增加除正式环境，其余环境统一输出
        if (ApplicationContextUtil.isProd()) {
            getStrategyMap().get(eventObject.getLevel().toString()).log(eventObject.getLoggerName(), eventObject.getMessage(), paramArray);
        } else {
            getStrategyMap().get(Level.DEBUG.toString()).log(org.slf4j.event.Level.valueOf(eventObject.getLevel().toString())
                    , eventObject.getLoggerName(), eventObject.getMessage(), paramArray);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //什么时候把@Log注解的Log类注入到Spring容器中的？ 通过自动装配注入到Spring
        applicationContext.getBeansWithAnnotation(Log.class).entrySet().iterator().forEachRemaining(entrySet -> strategyMap.put(((AbstractLog) entrySet.getValue()).level().toString(), (AbstractLog) entrySet.getValue()));
        //手动保存，用于多类加载的问题
        serialize.serialize();
    }

    private static class StrategySerialize extends AbstractSerializeUtil {

        Map<String, AbstractLog> cacheMap = getStrategyMap();

        public Map<String, AbstractLog> getStrategyMap() {
            Object deserializer = deserializer();
            if (deserializer != null) {
                cacheMap = (Map<String, AbstractLog>) deserializer;
            } else {
                cacheMap = new ConcurrentHashMap<>();
            }
            return cacheMap;
        }

        @Override
        public Object getEntity() {
            if (cacheMap.isEmpty()) {
                cacheMap = strategyMap;
            }
            return cacheMap;
        }

        @Override
        protected void serialize() {
            super.serialize();
        }
    }
}
