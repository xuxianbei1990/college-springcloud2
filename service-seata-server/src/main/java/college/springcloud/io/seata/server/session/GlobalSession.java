package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.common.util.XID;
import college.springcloud.io.seata.core.model.GlobalStatus;
import college.springcloud.io.seata.server.UUIDGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 18:11
 * Version:V1.0
 */
public class GlobalSession {

    private String xid;

    private long transactionId;

    private volatile GlobalStatus status;

    private String applicationId;

    private String transactionServiceGroup;

    private String transactionName;

    private int timeout;

    private Set<SessionLifecycleListener> lifecycleListeners = new HashSet<>();

    public static GlobalSession createGlobalSession(String applicationId, String txServiceGroup, String txName,
                                                    int timeout) {
        GlobalSession session = new GlobalSession(applicationId, txServiceGroup, txName, timeout);
        return session;
    }

    public GlobalSession(String applicationId, String transactionServiceGroup, String transactionName, int timeout) {
        this.transactionId = UUIDGenerator.generateUUID();
        this.status = GlobalStatus.Begin;

        this.applicationId = applicationId;
        this.transactionServiceGroup = transactionServiceGroup;
        this.transactionName = transactionName;
        this.timeout = timeout;
        this.xid = XID.generateXID(transactionId);
    }

    public void addSessionLifecycleListener(SessionLifecycleListener sessionLifecycleListener) {
        lifecycleListeners.add(sessionLifecycleListener);
    }

}
