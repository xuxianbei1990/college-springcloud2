package college.springcloud.common.enums;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:45
 * Version:V1.0
 */
//有点意思枚举支持接口
public enum ResultStatus implements IResultStatus {
    OK("200", "处理成功"),
    REMOTE_SERVICE_ERROR("601", "远程服务调用失败"),
    PARAMS_MISS("602", "缺少接口中必填参数"),
    PARAM_ERROR("603", "参数非法"),
    INTERNAL_SERVER_ERROR("604", "服务器内部错误"),
    NOT_IMPLEMENTED("605", "业务异常"),
    DATA_EXCEPTION("606", "数据异常");

    private String code;
    private String msg;

    ResultStatus(String code, String msg) {
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
