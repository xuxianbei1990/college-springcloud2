package college.springcloud.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/4/28
 * Time: 10:24
 * Version:V1.0
 * 服务注册白名单问题
 * https://blog.csdn.net/flying461/article/details/90784983
 */
@Component
public class EurekaStateListener {
    /**
     * 2.1  EurekaInstanceCanceledEvent                     当有服务下线时会执行（掉线顾名思义，就是某个服务关闭时触发）
     * 2.2  EurekaInstanceRegisteredEvent                   当有服务注册时会执行（注册中心检测到有新的服务注册进来时触发）
     * 2.3  EurekaInstanceRenewedEvent                     当有服务续约时会执行（服务设置了心跳时间，如果下一次心跳还正常，就会把服务续约的信息更新到自身的Eureka Server中，然后再同步到其它Eureka Server中）
     * 2.4  EurekaRegistryAvailableEvent                      Eureka 注册中心启动执行
     * <p>
     * 2.5  EurekaServerStartedEvent                            Eureka Server 启动时执行
     * 行云联盟警告通知
     * https://oapi.dingtalk.com/robot/send?access_token=93bfe80a0d00f1d824eaa4fd47d25b9c6f2cd86e2a3a1b6a0095720420cbc85c
     */
    //测试
    private String testDingDing = "https://oapi.dingtalk.com/robot/send?access_token=572378c091a831457c2d709b22d767eff282e7ce07bd25cf89a43b6a48659593";


    @Bean("restTemplate")
    public RestTemplate init() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @EventListener
    public void listen(EurekaInstanceCanceledEvent canceledEvent) {
        System.out.println("服务下线了：" + canceledEvent.getAppName());
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent registeredEvent) {
        System.out.println("服务上线了：" + registeredEvent.getInstanceInfo().getIPAddr());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        DingDingMsg msg = new DingDingMsg();
        msg.setMsgtype("text");
        msg.getText().setContent("服务监控 " + registeredEvent.getInstanceInfo().getIPAddr());
        HttpEntity<Map<String, String>> requestjson = new HttpEntity(beanToMap(msg), httpHeaders);
//        System.out.println(restTemplate.postForObject(testDingDing, requestjson
//                , String.class));

    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }
}
