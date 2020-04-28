package college.springcloud.spring.aeventdemo;

import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品下架事件
 *
 * @author: xuxianbei
 * Date: 2020/4/20
 * Time: 18:12
 * Version:V1.0
 */
public class GoodSkuSoldOutEvent<T> extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public GoodSkuSoldOutEvent(List<T> source) {
        super(source);
    }

    @Override
    public List<T> getSource() {
        if (super.getSource() instanceof List) {
            return (List<T>) super.getSource();
        }
        return new ArrayList<>();
    }
}
