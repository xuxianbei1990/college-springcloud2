package college.springcloud.io.seata.core.context;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:42
 * Version:V1.0
 */
public class RootContext {

    public static String getXID() {
//        String xid = CONTEXT_HOLDER.get(KEY_XID);
//        if (StringUtils.isNotBlank(xid)) {
//            return xid;
//        }
//
//        String xidType = CONTEXT_HOLDER.get(KEY_XID_INTERCEPTOR_TYPE);
//        if (StringUtils.isNotBlank(xidType) && xidType.indexOf("_") > -1) {
//            return xidType.split("_")[0];
//        }

        return null;
    }
}
