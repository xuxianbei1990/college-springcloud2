package college.seata.spring.annotation;

import college.seata.tm.api.TransactionalExecutor;
import college.seata.tm.api.TransactionalTemplate;
import college.seata.tm.api.transaction.NoRollbackRule;
import college.seata.tm.api.transaction.RollbackRule;
import college.seata.tm.api.transaction.TransactionInfo;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:07
 * Version:V1.0
 * <p>
 * 这个也可以用传统AOP实现。
 */
@Slf4j
public class GlobalTransactionalInterceptor implements MethodInterceptor {

    private final TransactionalTemplate transactionalTemplate = new TransactionalTemplate();

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class<?> targetClass = methodInvocation.getThis() != null ? AopUtils.getTargetClass(methodInvocation.getThis())
                : null;
        Method specificMethod = ClassUtils.getMostSpecificMethod(methodInvocation.getMethod(), targetClass);
        final Method method = BridgeMethodResolver.findBridgedMethod(specificMethod);

        final GlobalTransactional globalTransactionalAnnotation = getAnnotation(method, GlobalTransactional.class);
        if (globalTransactionalAnnotation != null) {
            return handleGlobalTransaction(methodInvocation, globalTransactionalAnnotation);
        } else {
            return methodInvocation.proceed();
        }
    }

    private Object handleGlobalTransaction(final MethodInvocation methodInvocation,
                                           final GlobalTransactional globalTrxAnno) throws Throwable {
        try {
            return transactionalTemplate.execute(new TransactionalExecutor() {
                @Override
                public Object execute() throws Throwable {
                    return methodInvocation.proceed();
                }

                public String name() {
                    String name = globalTrxAnno.name();
                    if (!StringUtils.isBlank(name)) {
                        return name;
                    }
                    return formatMethod(methodInvocation.getMethod());
                }

                @Override
                public TransactionInfo getTransactionInfo() {
                    TransactionInfo transactionInfo = new TransactionInfo();
                    transactionInfo.setTimeOut(globalTrxAnno.timeoutMills());
                    transactionInfo.setName(name());

                    //搞这么多回滚原则干嘛？
                    Set<RollbackRule> rollbackRules = new LinkedHashSet<>();
                    for (Class<?> rbRule : globalTrxAnno.rollbackFor()) {
                        rollbackRules.add(new RollbackRule(rbRule));
                    }
                    for (String rbRule : globalTrxAnno.rollbackForClassName()) {
                        rollbackRules.add(new RollbackRule(rbRule));
                    }
                    for (Class<?> rbRule : globalTrxAnno.noRollbackFor()) {
                        rollbackRules.add(new NoRollbackRule(rbRule));
                    }
                    for (String rbRule : globalTrxAnno.noRollbackForClassName()) {
                        rollbackRules.add(new NoRollbackRule(rbRule));
                    }
                    transactionInfo.setRollbackRules(rollbackRules);
                    return transactionInfo;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends Annotation> T getAnnotation(Method method, Class<T> clazz) {
        return method == null ? null : method.getAnnotation(clazz);
    }

    private String formatMethod(Method method) {
        StringBuilder sb = new StringBuilder(method.getName()).append("(");

        Class<?>[] params = method.getParameterTypes();
        int in = 0;
        for (Class<?> clazz : params) {
            sb.append(clazz.getName());
            if (++in < params.length) {
                sb.append(", ");
            }
        }
        return sb.append(")").toString();
    }
}
