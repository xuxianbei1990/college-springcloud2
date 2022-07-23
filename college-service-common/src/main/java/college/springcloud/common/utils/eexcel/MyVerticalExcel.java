package college.springcloud.common.utils.eexcel;

import java.lang.annotation.*;

/**
 * easyExcel 垂直读取
 *
 * @author: xuxianbei
 * Date: 2021/10/25
 * Time: 11:04
 * Version:V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyVerticalExcel {
    /**
     * 标题
     *
     * @return
     */
    String header();

    /**
     * 列名
     *
     * @return
     */
    String columnName();
}
