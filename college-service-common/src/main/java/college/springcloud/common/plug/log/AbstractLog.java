package college.springcloud.common.plug.log;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.event.Level;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/11/30
 * Time: 14:08
 * Version:V1.0
 */
public abstract class AbstractLog {

    public final static String SHORT_TRACE_ID = "traceId";

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String PACKAGE_LINK = ".";
    private Level LEVEL_FLAG = Level.TRACE;

    static {
        customMDC();
    }

    private static void customMDC() {
        try {
            //主要解决线程池  父线程变量无法传递给子线程变量
            ThreadLocal<Map<String, String>> newThreadLocal = new TransmittableThreadLocal<>();
            LogbackMDCAdapter mdcAdapter = new LogbackMDCAdapter();

            //这是一个有意思的方法，用反射替换掉原来的方法
            Field copyOnThreadLocal = mdcAdapter.getClass().getDeclaredField("copyOnThreadLocal");
            copyOnThreadLocal.setAccessible(true);
            copyOnThreadLocal.set(mdcAdapter, newThreadLocal);

            Field field = MDC.class.getDeclaredField("mdcAdapter");
            field.setAccessible(true);
            field.set(Field.class, mdcAdapter);
        } catch (Exception e) {

        }
    }

    /**
     * 跟踪Rpc
     */
    public static void traceRpc(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            return;
        }

        MDC.put(SHORT_TRACE_ID, traceId == null ? "" : traceId);
    }

    /**
     * 跟踪request
     */
    public static void traceRequest(HttpServletRequest request) {
        if (request == null) {
            return;
        }

        MDC.put(SHORT_TRACE_ID, getTraceIdByConvertRequest(request));
    }

    public static String getTraceId() {
        return MDC.get(SHORT_TRACE_ID);
    }


    /**
     * 通过踪转换request获取跟踪Id
     *
     * @param request 原来的请求
     */
    private static String getTraceIdByConvertRequest(HttpServletRequest request) {

        String shortTraceId = "";
        if (request == null) {
            return shortTraceId;
        }
        shortTraceId = request.getHeader(SHORT_TRACE_ID);
        if (!(shortTraceId == null || shortTraceId.isEmpty())) {
            return shortTraceId;
        }

        String originalTraceId = request.getHeader("user-agent");
        // TODO:这里进行traceID和spanId的跟进
        // 转换简短的hashcode作为traceId
        shortTraceId = originalTraceId == null ? "" : String.valueOf(originalTraceId.hashCode());

        return shortTraceId;

    }

    public void log(String className, String format, Object... arguments) {
        if (className == null || className.isEmpty() || format == null || format.length() == 0) {
            return;
        }

        if (level().toInt() < LEVEL_FLAG.toInt()) {
            return;
        }

        record(getFormatLog(className.substring(className.lastIndexOf(PACKAGE_LINK) + 1), format), arguments);
    }

    /**
     * 清除跟踪
     */
    public static void clearTrace() {
        MDC.clear();
    }

    private String getFormatLog(String className, String format) {
        return String.format("[%s] [%s] => %s", className, Thread.currentThread().getName(), format);
    }

    public abstract Level level();

    protected abstract void record(String format, Object... arguments);
}
