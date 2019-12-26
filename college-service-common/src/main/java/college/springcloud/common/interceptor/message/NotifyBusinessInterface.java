package college.springcloud.common.interceptor.message;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通知业务接口
 *
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:19
 * Version:V1.0
 */
public interface NotifyBusinessInterface {

    void setRequest(HttpServletRequest request);

    void setRequestMethod(RequestMethod requestMethod);

    void setRequestBody(String requestBody);

    void setGetParameterMap(Map<String, String[]> map);

    String getUri();

    ApplicationEvent getApplicationEvent();
}
