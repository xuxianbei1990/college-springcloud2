package college.springcloud.common.interceptor.message.business;

import college.springcloud.common.interceptor.message.NotifyBusinessInterface;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2019/12/24
 * Time: 10:05
 * Version:V1.0
 */
public abstract class AbstractNotifyBusiness implements NotifyBusinessInterface {

    /**
     * 在springframework  2.1.0.RELEASE 不可以删除
     */
    private String springKey = "主要针对spring框架，无实际用处，版本2.1.0.RELEASE";

    /**
     * 原始请求
     */
    protected HttpServletRequest request;
    /**
     * http动作
     */
    protected RequestMethod requestMethod;

    /**
     * 请求体
     */
    protected String requestBody;

    /**
     * 请求参数
     */
    protected Map<String, String[]> map;

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
        doSetOldKey();
    }


    @Override
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        doSetOldKey();
    }

    @Override
    public void setGetParameterMap(Map<String, String[]> map) {
        this.map = map;
    }

    protected abstract void doSetOldKey();

}
