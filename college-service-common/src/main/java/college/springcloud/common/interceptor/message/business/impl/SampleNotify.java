package college.springcloud.common.interceptor.message.business.impl;

import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
import college.springcloud.common.interceptor.message.business.NotifyHttpServletWrapper;
import college.springcloud.common.interceptor.message.business.WaitSendInfo;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 18:22
 * Version:V1.0
 */
@Component
public class SampleNotify extends AbstractNotifyBusiness implements NotifyBusinessInterface {


    private String[] urls = new String[]{"/mallpc/user/modifiyMobile"};


    @Override
    public String[] getUris() {
        return urls;
    }

    @Override
    public ApplicationEvent getApplicationEvent(WaitSendInfo waitSendInfo) {
        return new SampleEvent(waitSendInfo);
    }

    @Override
    protected void doAfterControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper) {
        //执行
    }

    @Override
    protected void doBeforeControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper) {
        //执行数据库操作，放入业务代码
       //然后在这里放入 oldStatus newStatus notifyHttpServletWrapper
        //这样虽然解决了 多线程问题，但是还有一个问题
        notifyHttpServletWrapper.getWaitSendInfo();
    }
}
