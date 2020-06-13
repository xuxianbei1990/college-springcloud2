package college.springcloud.gateway.loadbalance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

/**
 * @author: xuxianbei
 * Date: 2020/6/13
 * Time: 11:30
 * Version:V1.0
 * https://blog.csdn.net/cyxinda/article/details/98884205
 */
public class CustomLoadBalance extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        return null;
    }
}
