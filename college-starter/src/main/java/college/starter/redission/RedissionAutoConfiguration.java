package college.starter.redission;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Name
 *
 * @author xxb
 * Date 2020/7/27
 * VersionV1.0
 * @description
 */
@ConditionalOnClass(Redisson.class)
//把RedissonProperties注入到spring容器中
@EnableConfigurationProperties(RedissonProperties.class)
@Configuration
public class RedissionAutoConfiguration {

    @Bean
    RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        String prefix = "redis://";
        if (redissonProperties.isSsl()) {
            prefix = "rediss://";
        }
        config.useSingleServer().setAddress(prefix + redissonProperties.getHost() + ":"
                + redissonProperties.getPort()).setConnectTimeout(redissonProperties.getTimeout());
        return Redisson.create(config);
    }
}
