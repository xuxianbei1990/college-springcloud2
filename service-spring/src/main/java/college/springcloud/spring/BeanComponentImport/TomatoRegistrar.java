package college.springcloud.spring.BeanComponentImport;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * mybatis 就是通过这种方式的
 * User: xuxianbei
 * Date: 2019/11/5
 * Time: 16:06
 * Version:V1.0
 *
 */
public class TomatoRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(Tomato.class);
        registry.registerBeanDefinition("MyTomato", beanDefinition);
    }
}
