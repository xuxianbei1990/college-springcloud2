package college.springcloud.seata.scanner;

import college.springcloud.seata.util.SpringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author: xuxianbei
 * Date: 2020/1/21
 * Time: 14:04
 * Version:V1.0
 */
@Configuration
public class SeataAutoConfiguration {

    /**
     * 这个方法生效时间DI之后
     * 这个工具只有等到业务场景才能生效
     * @return
     */
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

    @Bean
    //依赖springUtils 一定要等到 springUtils 初始化好之后才生效
    @DependsOn({"springUtils"})
    //如果没有初始化过GlobalTransactionScanner 那么这里初始化，其实就是保证单例
    @ConditionalOnMissingBean(GlobalTransactionScanner.class)
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("xybbc-seata", "seata-tx");
    }
}
