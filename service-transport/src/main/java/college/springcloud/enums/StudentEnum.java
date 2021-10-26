package college.springcloud.enums;

import lombok.Getter;

/**
 * @author: xuxianbei
 * Date: 2021/9/29
 * Time: 11:45
 * Version:V1.0
 */
@Getter
public enum StudentEnum implements CodeNameInterface {

    HEALTHY_ONE(1, "生命垂危"),
    HEALTHY_TWO(2, "生病"),
    HEALTHY_THREE(3, "合格"),
    HEALTHY_FROUR(4, "亚健康"),
    HEALTHY_FIVE(5, "健康"),
    HEALTHY_SIX(6, "良好"),
    HEALTHY_SERVERN(7, "优秀");

    private int code;
    private String name;

    StudentEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
