package college.springcloud.student.enums;

import college.springcloud.common.enums.IResultStatus;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 16:37
 * Version:V1.0
 */
public enum StudentEnum implements IResultStatus {
    STUDENT_FALLBACK("0", "学生降级");

    private String code;
    private String msg;

    StudentEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
