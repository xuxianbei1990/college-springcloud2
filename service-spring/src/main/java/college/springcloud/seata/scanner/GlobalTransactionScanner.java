package college.springcloud.seata.scanner;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author: xuxianbei
 * Date: 2020/1/21
 * Time: 11:18
 * Version:V1.0
 * <p>
 * AbstractAutoProxyCreator 好像aop使用的，暂时不管
 */
public class GlobalTransactionScanner /*extends AbstractAutoProxyCreator*/ implements InitializingBean,
        ApplicationContextAware, DisposableBean {
    private static final int AT_MODE = 1;
    private static final int MT_MODE = 2;

    private static final int DEFAULT_MODE = AT_MODE + MT_MODE;
    private static final int ORDER_NUM = 1024;
    private MethodInterceptor interceptor;
    private final String applicationId;
    private final String txServiceGroup;
    private final int mode;

    public GlobalTransactionScanner(String applicationId, String txServiceGroup) {
        this(applicationId, txServiceGroup, DEFAULT_MODE);
    }

    public GlobalTransactionScanner(String applicationId, String txServiceGroup, int mode) {
//        setOrder(ORDER_NUM);
//        setProxyTargetClass(true);
        this.applicationId = applicationId;
        this.txServiceGroup = txServiceGroup;
        this.mode = mode;
    }

//    @Override
//    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
//        System.out.println("getAdvicesAndAdvisorsForBean");
//        return new Object[]{interceptor};
//    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext");
//        this.setBeanFactory(applicationContext);
    }
}
