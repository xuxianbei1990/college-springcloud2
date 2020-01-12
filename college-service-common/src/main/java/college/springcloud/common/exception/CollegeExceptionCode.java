package college.springcloud.common.exception;

import college.springcloud.common.enums.IResultStatus;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2020/1/10
 * Time: 17:59
 * Version:V1.0
 */
@Data
public class CollegeExceptionCode implements IResultStatus {
    public static final CollegeExceptionCode DATA_EXCEPTION = new CollegeExceptionCode("9994", "数据异常");
    public static final CollegeExceptionCode INTERNAL_SERVER_ERROR = new CollegeExceptionCode("604", "服务器内部错误");
    private String code;
    private String msg;

    public CollegeExceptionCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CollegeExceptionCode bulid(Object... args) {
        return new CollegeExceptionCode(this.getCode(), String.format(this.getMsg(), args));
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
