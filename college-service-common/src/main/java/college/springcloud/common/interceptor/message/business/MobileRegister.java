package college.springcloud.common.interceptor.message.business;

import college.springcloud.common.interceptor.message.NotifyBusinessInterface;
import college.springcloud.common.message.events.RegisterEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * 一期问题罗列：1.幂等问题。2.结果取值问题。3.没有key问题。4.自定义扩展参数问题。
 * 5.不能影响主业务（如果调用了抛异常的工具类）6.多个url拦截，难以匹配问题。
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:08
 * Version:V1.0
 */
@Component
public class MobileRegister extends AbstractNotifyBusiness implements NotifyBusinessInterface {
    private final static String[] urls = new String[]{"/notify/account/post/register"};


    @Override
    public String getUri() {
        return urls[0];
    }

    @Override
    public ApplicationEvent getApplicationEvent() {
        return new RegisterEvent(urls[0]);
    }


    @Override
    protected void doSetOldKey() {

    }
}
