package college.springcloud.common.exception;

import college.springcloud.common.enums.IResultStatus;

/**
 * 需要解决异常的延迟时间
 *
 * @author: xuxianbei
 * Date: 2020/5/22
 * Time: 14:08
 * Version:V1.0
 */
public class DateNeedResolveException extends NeedResolveException {
    private Integer date;

    public DateNeedResolveException(IResultStatus status, Integer date) {
        super(status);
        this.date = date;
    }

    public DateNeedResolveException(String msg, Integer date) {
        super(msg);
        this.date = date;
    }
}
