package college.springcloud.common.exception;

import college.springcloud.common.enums.IResultStatus;

/**
 * User: xuxianbei
 * Date: 2019/9/30
 * Time: 17:53
 * Version:V1.0
 */
public class BizException extends RuntimeException {
    IResultStatus status;

    public BizException(IResultStatus status) {
        super(status.getMsg());
        this.status = status;
    }

    public IResultStatus getStatus() {
        return this.status;
    }

    public void setStatus(IResultStatus status) {
        this.status = status;
    }
}
