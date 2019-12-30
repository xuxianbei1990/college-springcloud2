package college.springcloud.common.interceptor.message.business;

import org.springframework.context.ApplicationEvent;

/**
 * * 一期问题罗列：1.幂等问题。2.结果取值问题。3.没有key问题。4.自定义扩展参数问题。
 * * 6.多个url拦截，难以匹配问题
 *  ; 10. 前端调用，没有拦截；11.主从问题
 *
 * 解决问题：
 * 9。多个请求问题 多线程问题  7.从结果拿数据。。8.异常处理  5.不能影响主业务（如果调用了抛异常的工具类）
 * <p>
 * 通知业务接口
 *
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:19
 * Version:V1.0
 */
public interface NotifyBusinessInterface {

    /**
     * controller的URL
     *
     * @return
     */
    String[] getUris();

    /**
     * 合并url
     *
     * @return
     */
    String getUri();

    /**
     * 请求之后设置状态  主要用于一些场景从reques拿不到新的状态
     */
    void afterControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper);


    /**
     * 在请求之前设置状态
     */
    void beforControllerSetKey(NotifyHttpServletWrapper notifyHttpServletWrapper);

    /**
     * 定义事件
     *
     * @return
     */
    ApplicationEvent getApplicationEvent(WaitSendInfo waitSendInfo);
}
