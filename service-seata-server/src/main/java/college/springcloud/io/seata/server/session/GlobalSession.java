package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.common.util.XID;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.GlobalStatus;
import college.springcloud.io.seata.server.UUIDGenerator;
import college.springcloud.io.seata.server.store.SessionStorable;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 18:11
 * Version:V1.0
 */
@Data
public class GlobalSession implements SessionLifecycle, SessionStorable {

    private String xid;

    private long transactionId;

    private volatile GlobalStatus status;

    private long beginTime;

    private String applicationData;

    private boolean active = true;

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

    @Override
    public void begin() throws TransactionException {
        this.status = GlobalStatus.Begin;
        this.beginTime = System.currentTimeMillis();
        this.active = true;
        //就是这个 DefaultSessionManager
        for (SessionLifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onBegin(this);
        }
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] src) {

    }
}
