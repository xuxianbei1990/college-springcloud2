package college.springcloud.common.aspect;


import college.springcloud.common.annotation.Form;
import college.springcloud.common.cache.lock.CacheLock;
import college.springcloud.common.utils.LoginUtil;
import college.springcloud.common.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author nick
 * @decription 表单提交aop
 */

@Aspect
@Configuration
@Slf4j
public class FormCommitAspect {

    private static final String FORM_LOCK_PREFIX = "form:lock";

    private static final Long LOCK_EXPIRE_SECOND_TIME = 5L;

    private static final String TOKEN_HEADER = "formToken";

    @Autowired
    private CacheLock cacheLock;

    public FormCommitAspect() {
    }

    //@Pointcut("execution(* com.chenfan.sample.util.PageInfoUtil.*(..))")
    @Pointcut("execution(* college.springcloud.*.controller..*.*(..))")
    public void formCommitCut() {

    }

    @Before("formCommitCut()")
    public void verifyToken(JoinPoint point) {
        lockOrRelease(point, true);
    }

    @After("formCommitCut()")
    public void releaseToken(JoinPoint point) {
        lockOrRelease(point, false);
    }

    private void lockOrRelease(JoinPoint point, boolean isLock) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            //验证注解
            if (!method.isAnnotationPresent(Form.class)) {
                return;
            }
            // 拿到token
            String token = RequestHolder.getRequest().getHeader(TOKEN_HEADER);
            if (StringUtils.isNotBlank(token)) {
                //key 类名+方法名+userId hash
                String methodName = method.getName();
                String clzName = point.getTarget().getClass().getSimpleName();
                Long userId = LoginUtil.getCurrentAccount().getFadminId();
                //哈希碰撞
                String key = clzName + methodName + userId;
                String lock = FORM_LOCK_PREFIX + key.hashCode();
                if (isLock) {
                    if (!cacheLock.tryLock(lock, token, LOCK_EXPIRE_SECOND_TIME, TimeUnit.SECONDS)) {
                        log.error("表单重复提交,class===>{},method===>{},userId:{}", clzName, methodName, userId);
                        throw new RuntimeException("FORM_COMMIT_REPEAT");
                    }
                } else {
                    cacheLock.releaseLock(lock, token);
                }
            }
        } catch (Exception e) {
            log.error("系统异常", e.fillInStackTrace());
            throw new RuntimeException("FORM_COMMIT_REPEAT");
        }
    }
}
