package college.springcloud.common.interceptor.message.business;

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

    /**
     * 保留原始请求
     * @param request
     */
    void setRequest(HttpServletRequest request);

    /**
     * 请求方式
     * @param requestMethod
     */
    void setRequestMethod(RequestMethod requestMethod);

    /**
     * 请求体
     * @param requestBody
     */
    void setRequestBody(String requestBody);

    /**
     * 请求参数
     * @param map
     */
    void setGetParameterMap(Map<String, String[]> map);

    /**
     * 业务ID
     * @return
     */
    String getBusinessId();

    /**
     * 目标对象id 供应商/用户
     * @return
     */
    Long getTargetId();

    /**
     * 旧key
     * @return
     */
    String getOldKey();

    /**
     * 新key
     * @return
     */
    String getNewKey();


    /**
     * controller的URL
     * @return
     */
    String[] getUris();

    /**
     * 合并url
     * @return
     */
    String getUri();

    /**
     * 请求之后设置key  主要用于一些场景从reques拿不到新的状态
     */
    void afterControllerSetKey();

    /**
     * 定义事件
     * @return
     */
    ApplicationEvent getApplicationEvent();
}
