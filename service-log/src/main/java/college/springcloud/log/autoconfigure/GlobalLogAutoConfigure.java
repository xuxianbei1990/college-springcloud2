package college.springcloud.log.autoconfigure;

import college.springcloud.log.plug.log.level.ErrorLog;
import college.springcloud.log.plug.log.level.InfoLog;
import college.springcloud.log.plug.log.level.TraceLog;
import college.springcloud.log.plug.log.level.WarnLog;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuxianbei
 * Date: 2021/3/3
 * Time: 18:24
 * Version:V1.0
 */
@Configuration
@ConditionalOnProperty(
        name = {"spring.application.name"},
        matchIfMissing = true
)
public class GlobalLogAutoConfigure {

    private static final String INTERCEPTOR_PATH = "/*";

    @Bean
    public TraceLog traceLog() {
        return new TraceLog();
    }

    @Bean
    public WarnLog warnLog() {
        return new WarnLog();
    }

    @Bean
    public ErrorLog errorLog() {
        return new ErrorLog();
    }


    @Bean
    public InfoLog infoLog() {
        return new InfoLog();
    }

}
