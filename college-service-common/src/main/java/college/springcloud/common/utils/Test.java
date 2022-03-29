package college.springcloud.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 测试类
 *
 * @author: xuxianbei
 * Date: 2021/12/28
 * Time: 11:12
 * Version:V1.0
 */
public class Test {


    /**
     * 随机填充数值
     *
     * @param bean
     * @param tclass
     */
    public static void randomFillProperty(Object bean, Class<?> tclass) {
        for (Field declaredField : tclass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            try {
                if (declaredField.getType().equals(BigDecimal.class)) {
                    BigDecimal decimal = BigDecimal.valueOf(Math.random())
                            .multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    declaredField.set(bean, decimal);
                }
                if (declaredField.getType().equals(String.class)) {
                    declaredField.set(bean, "xxb");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
