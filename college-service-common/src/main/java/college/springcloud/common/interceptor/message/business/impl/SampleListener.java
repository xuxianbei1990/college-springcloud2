package college.springcloud.common.interceptor.message.business.impl;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2019/12/24
 * Time: 11:52
 * Version:V1.0
 */

@Component
public class SampleListener implements ApplicationListener<SampleEvent> {


    @Override
    public void onApplicationEvent(SampleEvent event) {
        System.out.println("receive Listener");
        //配置Stream 发送信息
    }
}
