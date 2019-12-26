package college.springcloud.common.interceptor.message.business.impl;

import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
import college.springcloud.common.interceptor.message.business.WaitSendInfo;
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

    protected WaitSendInfo waitSendInfo = new WaitSendInfo();
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
        doSetKey();
        afterSetKey();
    }

    private void afterSetKey() {
        beforControllerSetKey();
        waitSendInfo.setTargetId(getTargetId());
        waitSendInfo.setBusinessId(getBusinessId());
        waitSendInfo.setNewKey(getNewKey());
        waitSendInfo.setObject(getOldKey());
    }


    @Override
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        doSetKey();
        afterSetKey();
    }

    @Override
    public void setGetParameterMap(Map<String, String[]> map) {
        this.map = map;
    }


    /**
     * 作废理由名字不好  改为beforControllerSetKey
     */
    @Deprecated
    protected void doSetKey() {

    }

    protected void beforControllerSetKey() {

    }

     public void afterControllerSetKey() {

    }

    @Override

    public String getUri() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String uris : getUris()) {
            stringBuilder.append(uris).append(",");
        }
        return stringBuilder.toString();
    }
}
