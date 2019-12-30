package college.springcloud.common.interceptor.message.business;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 业务工厂，通过不同类型的业务返回不同的结果
 *
 * @author: xuxianbei
 * Date: 2019/12/23
 * Time: 17:01
 * Version:V1.0
 */
@Component
public class BusinessFactory {

    @Autowired
    ApplicationContext applicationContext;

    public NotifyBusinessInterface getByUrI(String uri) {
        Map<String, NotifyBusinessInterface> map = applicationContext.getBeansOfType(NotifyBusinessInterface.class);
        Optional<NotifyBusinessInterface> optionalvalue = map.entrySet().stream().
                filter(t -> ArrayUtils.contains(t.getValue().getUris(), uri)).map(entry -> entry.getValue()).findFirst();
        if (optionalvalue.isPresent()) {
            return optionalvalue.get();
        } else {
            return null;
        }
    }
}
