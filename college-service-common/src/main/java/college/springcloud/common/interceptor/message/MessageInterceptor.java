package college.springcloud.common.interceptor.message;

import college.springcloud.common.interceptor.message.business.BusinessFactory;
import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
import college.springcloud.common.interceptor.message.business.NotifyHttpServletWrapper;
import college.springcloud.common.interceptor.message.filter.RepeatReadFilter;
import college.springcloud.common.interceptor.message.filter.RepeatReadRequestWrapper;
import college.springcloud.common.interceptor.message.filter.RepeatWriteResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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

    private static final String NOTIFYBUSINESSINTERFACEIDDATA = "notifyBusinessInterfaceIdData";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!RepeatReadFilter.CheckoutMethod(request)) {
            return true;
        }
        if (request.getAttribute(NOTIFYBUSINESSINTERFACEID) != null || request.getAttribute(NOTIFYBUSINESSINTERFACEIDDATA) != null) {
            throw new RuntimeException("通知业务attribute:notifyBusinessInterfaceId 或者 notifyBusinessInterfaceIdData 被使用");
        }
        String uri = request.getRequestURI();
        NotifyBusinessInterface notifyBusinessInterface = businessFactory.getByUrI(uri);
        if (Objects.isNull(notifyBusinessInterface)) {
            return true;
        }
        NotifyHttpServletWrapper notifyHttpServletWrapper = new NotifyHttpServletWrapper();
        notifyHttpServletWrapper.setRequest(request);
        if (Objects.equals(RequestMethod.POST.name(), request.getMethod())) {
            //从消息体中获取
            Assert.isTrue(request instanceof RepeatReadRequestWrapper, "存在其他的拦截器");
            RepeatReadRequestWrapper repeatReadRequestWrapper = (RepeatReadRequestWrapper) request;
            notifyHttpServletWrapper.setRequestMethod(RequestMethod.POST);
            notifyHttpServletWrapper.setRequestBody(repeatReadRequestWrapper.getBody());
        } else if (Objects.equals(RequestMethod.GET.name(), request.getMethod())) {
            //从参数中获取
            Map<String, String[]> map = request.getParameterMap();
            notifyHttpServletWrapper.setRequestMethod(RequestMethod.GET);
            notifyHttpServletWrapper.setParameterMap(map);
        }
        request.setAttribute(NOTIFYBUSINESSINTERFACEID, notifyBusinessInterface);
        request.setAttribute(NOTIFYBUSINESSINTERFACEIDDATA, notifyHttpServletWrapper);
        notifyBusinessInterface.beforControllerSetKey(notifyHttpServletWrapper);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!RepeatReadFilter.CheckoutMethod(request)) {
            return;
        }
        NotifyBusinessInterface notifyBusinessInterface = (NotifyBusinessInterface) request.getAttribute(NOTIFYBUSINESSINTERFACEID);
        NotifyHttpServletWrapper notifyHttpServletWrapper = (NotifyHttpServletWrapper) request.getAttribute(NOTIFYBUSINESSINTERFACEIDDATA);
        if (Objects.isNull(notifyBusinessInterface) || Objects.isNull(notifyHttpServletWrapper)) {
            return;
        }
        Assert.isTrue(response instanceof RepeatWriteResponseWrapper, "存在其他的拦截器");
        String responseBody = new String(((RepeatWriteResponseWrapper) response).getBody());
        notifyHttpServletWrapper.setResponseBody(responseBody);
        notifyBusinessInterface.afterControllerSetKey(notifyHttpServletWrapper);
        applicationContext.publishEvent(notifyBusinessInterface.getApplicationEvent(notifyHttpServletWrapper.getWaitSendInfo()));

    }
}
