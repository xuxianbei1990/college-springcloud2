package college.seata.tm.api.transaction;

import org.apache.commons.lang.StringUtils;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:30
 * Version:V1.0
 */
public class RollbackRule {

    private final String exceptionName;

    public int getDepth(Throwable ex) {
        return getDepth(ex.getClass(), 0);
    }

    private int getDepth(Class<?> exceptionClass, int depth) {
        if (exceptionClass.getName().contains(this.exceptionName)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass == Throwable.class) {
            return -1;
        }
        return getDepth(exceptionClass.getSuperclass(), depth + 1);
    }

    public RollbackRule(String exceptionName) {
        if (StringUtils.isBlank(exceptionName)) {
            throw new IllegalArgumentException("'exceptionName' cannot be null or empty");
        }
        this.exceptionName = exceptionName;
    }

    public RollbackRule(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("'clazz' cannot be null");
        }
        if (!Throwable.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    "Cannot construct rollback rule from [" + clazz.getName() + "]: it's not a Throwable");
        }
        this.exceptionName = clazz.getName();
    }
}
