package college.springcloud.common.interceptor.message.business;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通知服务中介
 *
 * @author: xuxianbei
 * Date: 2019/12/30
 * Time: 20:23
 * Version:V1.0
 */
@Data
public class NotifyHttpServletWrapper {

    /**
     * 原始请求
     */
    private HttpServletRequest request;
    /**
     * http动作
     */
    private RequestMethod requestMethod;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应体
     */
    protected String responseBody;

    /**
     * 请求参数
     */
    private Map<String, String[]> ParameterMap;

    /**
     * 待发送信息
     */
    private WaitSendInfo waitSendInfo = new WaitSendInfo();
}
