package college.springcloud.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:08
 * Version:V1.0
 */
public abstract class AbstractAutoConfigure implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
