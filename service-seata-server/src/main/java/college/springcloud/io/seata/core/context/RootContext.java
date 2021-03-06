package college.springcloud.io.seata.core.context;

import org.apache.commons.lang.StringUtils;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:42
 * Version:V1.0
 */
public class RootContext {

    private static ContextCore CONTEXT_HOLDER = new ThreadLocalContextCore();
    public static final String KEY_XID = "TX_XID";
    public static final String KEY_XID_INTERCEPTOR_TYPE = "tx-xid-interceptor-type";


    public static String getXID() {
        String xid = CONTEXT_HOLDER.get(KEY_XID);
        if (StringUtils.isNotBlank(xid)) {
            return xid;
        }

        String xidType = CONTEXT_HOLDER.get(KEY_XID_INTERCEPTOR_TYPE);
        if (StringUtils.isNotBlank(xidType) && xidType.indexOf("_") > -1) {
            return xidType.split("_")[0];
        }

        return null;
    }

    public static String unbind() {
//        String xid = CONTEXT_HOLDER.remove(KEY_XID);
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("unbind {} ", xid);
//        }
        return "xid";
    }

    public static boolean inGlobalTransaction() {
        return CONTEXT_HOLDER.get(KEY_XID) != null;
    }
}
