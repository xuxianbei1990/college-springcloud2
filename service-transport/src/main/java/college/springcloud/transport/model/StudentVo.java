package college.springcloud.transport.model;

import college.springcloud.annotation.AutoExchange;
import college.springcloud.enums.CodeNameInterface;
import college.springcloud.enums.StudentEnum;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 11:37
 * Version:V1.0
 */
@Data
public class StudentVo {
    /**
     * 1：生命垂危；2：生病；3：合格；4：亚健康；5：健康；6：良好；7：优秀；8：超人类；9：超人；10：撒亚人；11：超级撒亚人；12：赛亚之神
     */
    @AutoExchange(value = StudentEnum.class)
    private Integer healthy;

    private String healthyName;
}
