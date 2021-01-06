package college.springcloud.common.config.IdempotentInterceptorAdapter;

import college.springcloud.common.annotation.Idempotent;
import college.springcloud.common.utils.IpUtil;
import college.springcloud.common.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2021/1/5
 * Time: 11:37
 * Version:V1.0
 */
@Component
public class IdempotentInterceptorAdapter extends HandlerInterceptorAdapter implements ApplicationContextAware,
        HandlerExceptionResolver {

    private Map<String, Integer> redis = new ConcurrentHashMap();

    private final String FAILURE_MSG = "{\n" +
            "    \"data\": \"重复登陆\",\n" +
            "    \"success\": false\n" +
            "}";

    @Autowired
    private ApplicationContext applicationContext;

    @Nullable
    protected HandlerExecutionChain getHandler(HttpServletRequest request, List<HandlerMapping> handlerMappings) throws Exception {
        if (handlerMappings != null) {
            for (HandlerMapping mapping : handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        DispatcherServlet dispatcherServlet = applicationContext.getBean(DispatcherServlet.class);
        HandlerExecutionChain mappedHandler = getHandler(request, dispatcherServlet.getHandlerMappings());
        if (Objects.nonNull(mappedHandler) && Objects.nonNull(mappedHandler.getHandler())) {
            if (mappedHandler.getHandler() instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) mappedHandler.getHandler();
                if (handlerMethod.getMethod().isAnnotationPresent(Idempotent.class)) {
                    String key = getKey(request);
                    boolean result = Objects.isNull(redis.putIfAbsent(key, 1));
                    //这里还是加一个超时的好，防止永久死锁
                    if (!result) {
//                        throw new RuntimeException("重复了");
                        errorMsg(response);
                    }
                    return result;
                }
            }

        }
        return super.preHandle(request, response, handler);
    }

    private void errorMsg(HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(FAILURE_MSG.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private String getKey(HttpServletRequest request) {
        return request.getRequestURI() + IpUtil.getRealIp(request) + "userId";
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        redis.remove(getKey(request));
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        redis.remove(getKey(request));

        return null;
    }
}
