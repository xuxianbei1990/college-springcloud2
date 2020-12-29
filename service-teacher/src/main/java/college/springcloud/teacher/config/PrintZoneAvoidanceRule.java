package college.springcloud.teacher.config;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

/**
 * @author: xuxianbei
 * Date: 2020/12/29
 * Time: 17:15
 * Version:V1.0
 */
public class PrintZoneAvoidanceRule extends ZoneAvoidanceRule {
    @Override
    public Server choose(Object key) {
        Server server = super.choose(key);
        System.out.println(server.getMetaInfo().getInstanceId());
        return server;
    }
}
