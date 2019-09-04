package college.springcloud.common.utils;

import college.springcloud.common.enums.IResultStatus;
import college.springcloud.common.enums.ResultStatus;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 14:35
 * Version:V1.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 6135238466273811975L;
    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("消息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("额外数据")
    private Map<String, Object> extra;

    public Result() {
    }

    public Result<T> addExtraIfTrue(boolean bool, String key, Object value) {
        if (bool) {
            this.addExtra(key, value);
        }

        return this;
    }

    public Result<T> addExtra(String key, Object value) {
        this.extra = (Map)(this.extra == null ? new HashMap(16) : this.extra);
        this.extra.put(key, value);
        return this;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getExtra() {
        return this.extra;
    }

    public Result<T> setExtra(Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }

    public static <T> Result<T> success() {
        return success((T)null);
    }

    public static Result failure(IResultStatus resultStatus) {
        return (new Result()).setSuccess(false).setCode(resultStatus.getCode()).setMsg(resultStatus.getMsg());
    }

    public static Result failure(String code, String msg) {
        return (new Result()).setSuccess(false).setCode(code).setMsg(msg);
    }

    public static <T> Result<T> success(T obj) {
        return (new Result()).setData(obj).setCode(ResultStatus.OK.getCode()).setMsg(ResultStatus.OK.getMsg()).setSuccess(true);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> s) throws X {
        if (this.isSuccess()) {
            return this.getData();
        } else {
            throw (X)s.get();
        }
    }
}
