package college.springcloud.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2020/5/21
 * Time: 15:32
 * Version:V1.0
 */
@Component
public class ApplicaitonContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    private ApplicaitonContextHolder() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
