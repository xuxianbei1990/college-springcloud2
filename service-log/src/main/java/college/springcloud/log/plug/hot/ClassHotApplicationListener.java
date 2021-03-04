package college.springcloud.log.plug.hot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:10
 * Version:V1.0
 */
public class ClassHotApplicationListener implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ClassHotLoader.getInstance().addApplicationContext((ConfigurableApplicationContext) applicationContext);
    }
}
