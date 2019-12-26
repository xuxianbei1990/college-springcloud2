package college.springcloud.common.interceptor.message;

import college.springcloud.common.interceptor.message.business.BusinessFactory;
import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
import college.springcloud.common.interceptor.message.filter.RepeatReadRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author: xuxianbei
 * Date: 2019/12/20
 * Time: 16:13
 * Version:V1.0
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private BusinessFactory businessFactory;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 业务key
     */
    private static final String NOTIFYBUSINESSINTERFACEID = "notifyBusinessInterfaceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!CheckoutMethod(request)) {
            return true;
        }
        String uri = request.getRequestURI();
        //可以优化为从request 获取 减少IO请求
        NotifyBusinessInterface notifyBusinessInterface = businessFactory.getByUrI(uri);
        if (Objects.isNull(notifyBusinessInterface)) {
            return true;
        }
        notifyBusinessInterface.setRequest(request);
        if (Objects.equals(RequestMethod.POST.name(), request.getMethod())) {
            //从消息体中获取
            RepeatReadRequestWrapper repeatReadRequestWrapper = new RepeatReadRequestWrapper(request);
            notifyBusinessInterface.setRequestMethod(RequestMethod.POST);
            notifyBusinessInterface.setRequestBody(repeatReadRequestWrapper.getBody());
        } else if (Objects.equals(RequestMethod.GET.name(), request.getMethod())) {
            //从参数中获取
            Map<String, String[]> map = request.getParameterMap();
            notifyBusinessInterface.setRequestMethod(RequestMethod.GET);
            notifyBusinessInterface.setGetParameterMap(map);
        }
        request.setAttribute(NOTIFYBUSINESSINTERFACEID, notifyBusinessInterface);
        return true;
    }

    /**
     * 校验合法
     *
     * @param request
     * @return
     */
    public static boolean CheckoutMethod(HttpServletRequest request) {
        return Objects.equals(request.getContentType(), "application/json") ||
                Objects.equals(request.getContentType(), "text/xml");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!CheckoutMethod(request)) {
            return;
        }
        NotifyBusinessInterface notifyBusinessInterface = (NotifyBusinessInterface) request.getAttribute(NOTIFYBUSINESSINTERFACEID);
        if (Objects.nonNull(notifyBusinessInterface)) {
            //调用目标方法的从数据库获取  可以优化为从response 获取 减少IO请求
            notifyBusinessInterface.afterControllerSetKey();
            //发布事件从目标中拿到事件
            applicationContext.publishEvent(notifyBusinessInterface.getApplicationEvent());
        }
    }
}
