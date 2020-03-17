package college.seata;

import college.seata.rm.RMClient;
import college.seata.rm.datasource.DataSourceProxy;
import college.seata.spring.annotation.datasource.DataSourceProxyHolder;
import college.seata.tm.TMClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;



/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 15:34
 * Version:V1.0
 */
@Slf4j
public class GlobalTransactionScanner {


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //这段代码的意思就是拦截数据源
        if (bean instanceof DataSource && !(bean instanceof DataSourceProxy)) {
            if (log.isInfoEnabled()) {
                log.info("Auto proxy of [{}]", beanName);
            }
            DataSourceProxy dataSourceProxy = DataSourceProxyHolder.get().putDataSource((DataSource) bean);
            Class<?>[] interfaces = SpringProxyUtils.getAllInterfaces(bean);
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Method m = BeanUtils.findDeclaredMethod(DataSourceProxy.class, method.getName(), method.getParameterTypes());
                    if (null != m) {
                        return m.invoke(dataSourceProxy, args);
                    } else {
                        boolean oldAccessible = method.isAccessible();
                        try {
                            method.setAccessible(true);
                            return method.invoke(bean, args);
                        } finally {
                            //recover the original accessible for security reason
                            method.setAccessible(oldAccessible);
                        }
                    }
                }
            });

        }
        return null;
    }

    public static void main(String[] args) {
        TMClient.init("1", "2");
        RMClient.init("1", "2");

        //postProcessAfterInitialization  seata的做法是初始化之后做代理
        //这种aop还是有点意思的。  如果是我也只能这么做AbstractAutoProxyCreator

    }
}
