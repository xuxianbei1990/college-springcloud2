package college.springcloud.common.config;

import college.springcloud.common.plug.hot.ClassHotApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:09
 * Version:V1.0
 */
public class GlobalHotAutoConfigure extends AbstractAutoConfigure {

    @Bean
    public ClassHotApplicationListener classHotApplicationListener() {
        return new ClassHotApplicationListener();
    }
}
