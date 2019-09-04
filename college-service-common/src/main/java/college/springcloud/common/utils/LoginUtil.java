package college.springcloud.common.utils;

import college.springcloud.common.model.vo.AccountLoginVo;
import org.springframework.util.Assert;

/**
 * User: xuxianbei
 * Date: 2019/9/4
 * Time: 14:09
 * Version:V1.0
 */
public class LoginUtil {

    private static final ThreadLocal<AccountLoginVo> THREADLOCAL = new ThreadLocal<>();

    private LoginUtil() {

    }

    public static ThreadLocal<AccountLoginVo> getThreadLocal() {
        return THREADLOCAL;
    }

    public static AccountLoginVo getCurrentAccount() {
        AccountLoginVo accountLoginVo = THREADLOCAL.get();
        Assert.isTrue(accountLoginVo != null, "token无效");
        return accountLoginVo;
    }

}
