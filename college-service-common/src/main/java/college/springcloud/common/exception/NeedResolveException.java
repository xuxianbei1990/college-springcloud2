package college.springcloud.common.exception;

import college.springcloud.common.enums.IResultStatus;

/**
 * 需要解决的异常
 *
 *
 * @author: xuxianbei
 * Date: 2020/5/22
 * Time: 14:06
 * Version:V1.0
 */
public class NeedResolveException extends BizException {
    public NeedResolveException(IResultStatus status) {
        super(status);
    }

    public NeedResolveException(String msg) {
        super(msg);
    }
}
