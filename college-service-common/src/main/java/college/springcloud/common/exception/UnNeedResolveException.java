package college.springcloud.common.exception;

import college.springcloud.common.enums.IResultStatus;

/**
 * 不需要解决异常
 *
 * @author: xuxianbei
 * Date: 2020/5/22
 * Time: 14:07
 * Version:V1.0
 */
public class UnNeedResolveException extends BizException {
    public UnNeedResolveException(IResultStatus status) {
        super(status);
    }

    public UnNeedResolveException(String msg) {
        super(msg);
    }
}
