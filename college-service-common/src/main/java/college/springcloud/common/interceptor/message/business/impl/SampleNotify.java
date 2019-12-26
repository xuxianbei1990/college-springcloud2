package college.springcloud.common.interceptor.message.business.impl;

import college.springcloud.common.interceptor.message.business.NotifyBusinessInterface;
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
    public String getBusinessId() {
        //存放唯一标识：例如：用户id
        return null;
    }

    @Override
    public Long getTargetId() {
        return null;
    }

    //可以优化为从request 获取 减少IO请求
    @Override
    public String getOldKey() {
        //存放老的key;例如手机号码
        return null;
    }

    @Override
    public String getNewKey() {
        //存放新的key;例如:手机号码
        return null;
    }

    @Override
    public String[] getUris() {
        return urls;
    }

    @Override
    public ApplicationEvent getApplicationEvent() {
        return new SampleEvent(waitSendInfo);
    }

    @Override
    protected void doSetKey() {
        //执行数据库操作
    }
}
