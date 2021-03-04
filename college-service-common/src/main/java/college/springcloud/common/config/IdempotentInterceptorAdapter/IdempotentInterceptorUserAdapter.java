package college.springcloud.common.config.IdempotentInterceptorAdapter;

import college.springcloud.common.annotation.Idempotent;
import college.springcloud.common.annotation.NotIdempotent;
import college.springcloud.common.cache.lock.CacheLock;
import college.springcloud.common.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户幂等拦截器
 *
 * @author: xuxianbei
 * Date: 2021/1/5
 * Time: 10:04
 * Version:V1.0
 */
//@RefreshScope
@Component
public class IdempotentInterceptorUserAdapter extends HandlerInterceptorAdapter {

    @Autowired
    private CacheLock cacheLock;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 默认超时时间，放置死锁
     */
    @Value("${idempotent.timeout:120}")
    private Integer timeOut;

    /**
     * 开启定制幂等：1：开启定制幂等；0：开启全局幂等
     */
    @Value("${idempotent.custom:0}")
    private Integer custom;


    private final String FAILURE_MSG = "{\n" +
            "    \"data\": \"重复登陆\",\n" +
            "    \"success\": false\n" +
            "}";


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
                if (customIdempotent(handlerMethod)) {
                    String key = getKey(request);
                    boolean result = cacheLock.tryLock(key, cacheLock.defaultValue(), timeOut, TimeUnit.SECONDS);
                    if (!result) {
                        errorMsg(response);
                    }
                    return result;
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 定制幂等；true 启动幂等；
     *
     * @param handlerMethod
     * @return
     */
    private boolean customIdempotent(HandlerMethod handlerMethod) {
        return (custom == 0 && !handlerMethod.getMethod().isAnnotationPresent(NotIdempotent.class))
                || (custom == 1 && handlerMethod.getMethod().isAnnotationPresent(Idempotent.class));
    }

    private void errorMsg(HttpServletResponse response) {
//        throw SampleBizState.USER_IDEMPOTENT_ERROR;
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(FAILURE_MSG.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getKey(HttpServletRequest request) {
//        if (Objects.isNull(UserVoConstextHolder.getUserVo())) {
//            return Strings.EMPTY;
//        }
        return "->uri:" + request.getRequestURI() + "->ip:" + IpUtil.getRealIp(request) +
                "->userId:" + "UserVoConstextHolder.getUserVo().getUserId()";
    }

    /**
     * 结束
     * 异常也会走这里的，不用担心
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        cacheLock.releaseLock(getKey(request), cacheLock.defaultValue());
        super.afterCompletion(request, response, handler, ex);
    }
}
