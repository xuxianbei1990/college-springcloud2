package college.seata.tm.api.transaction;

import java.util.Collections;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:57
 * Version:V1.0
 */
public class TransactionHookManager {

    private static final ThreadLocal<List<TransactionHook>> LOCAL_HOOKS = new ThreadLocal<>();

    public static List<TransactionHook> getHooks() throws IllegalStateException {
        List<TransactionHook> hooks = LOCAL_HOOKS.get();

        if (hooks == null || hooks.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(hooks);
    }
}
