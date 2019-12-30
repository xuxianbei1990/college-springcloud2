package college.springcloud.common.interceptor.message.business.impl;

import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
import college.springcloud.common.interceptor.message.business.NotifyHttpServletWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuxianbei
 * Date: 2019/12/24
 * Time: 10:05
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractNotifyBusiness implements NotifyBusinessInterface {

    /**
     * 在springframework  2.1.0.RELEASE 不可以删除
     */
    private String springKey = "主要针对spring框架，无实际用处，版本2.1.0.RELEASE";

    public void beforControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper) {
        try {
            doBeforeControllerSetKey(notifyHttpServletWrapper);
        } catch (Exception e) {
            log.info("beforControllerSetKey Exception:", e);
        }
    }

    public void afterControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper) {
        try {
            doAfterControllerSetKey(notifyHttpServletWrapper);
        } catch (Exception e) {
            log.info("afterControllerSetKey Exception:", e);
        }
    }

    protected abstract void doAfterControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper);

    protected abstract void doBeforeControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper);


    @Override
    public String getUri() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String uris : getUris()) {
            stringBuilder.append(uris).append(",");
        }
        return stringBuilder.toString();
    }
}
