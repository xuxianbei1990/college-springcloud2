package college.springcloud.common.interceptor.message;

import college.springcloud.common.interceptor.message.business.BusinessFactory;
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
 * 消息拦截
 *
 * @author: xuxianbei
 * Date: 2019/12/19
 * Time: 20:41
 * Version:V1.0
 * 平均增加2次数据库连接。
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BusinessFactory businessFactory;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!CheckoutMethod(request)) {
            return true;
        }
        /**
         * 1. 获取url和修改手机号码的url对比
         * 2. 从数据库中找到该手机号码
         * 3. 放到本机缓存中（问题1.消耗太多本机内存）
         */
        String uri = request.getRequestURI();
        //这时候我可以拿到他的id，业务uri，
        NotifyBusinessInterface baseParameters = businessFactory.getByUrI(uri);
        if (Objects.isNull(baseParameters)) {
            return true;
        }
        baseParameters.setRequest(request);
        if (Objects.equals(RequestMethod.POST.name(), request.getMethod())) {
            //从消息体中获取
            RepeatReadRequestWrapper repeatReadRequestWrapper = new RepeatReadRequestWrapper(request);
            baseParameters.setRequestMethod(RequestMethod.POST);
            baseParameters.setRequestBody(repeatReadRequestWrapper.getBody());
        } else if (Objects.equals(RequestMethod.GET.name(), request.getMethod())) {
            //从参数中获取
            Map<String, String[]> map = request.getParameterMap();
            baseParameters.setRequestMethod(RequestMethod.GET);
            baseParameters.setGetParameterMap(map);
        }
        //放到header
        request.setAttribute("id", baseParameters);
        return true;
    }

    public static boolean CheckoutMethod(HttpServletRequest request) {
        return Objects.equals(request.getContentType(), "application/json") ||
                Objects.equals(request.getContentType(), "text/xml");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!CheckoutMethod(request)) {
            return;
        }
        /**
         * 1.a.尝试从response中获取结果状态。例如；1：表示修改成功；有点性能高，缺点；依赖强
         *   b.从数据库中获取修改后的手机号码；与上相反
         * 2.从缓存中找到该手机号码，发送事件
         */
        //从header中取出
        NotifyBusinessInterface baseParameters = (NotifyBusinessInterface) request.getAttribute("id");
        //调用目标方法的从数据库获取
        //发布事件  从目标中拿到事件
        applicationContext.publishEvent(baseParameters.getApplicationEvent());
        //统计监听事件，发送消息
    }
}
