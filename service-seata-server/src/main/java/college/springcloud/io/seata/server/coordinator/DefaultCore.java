package college.springcloud.io.seata.server.coordinator;

import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import college.springcloud.io.seata.server.session.GlobalSession;
import college.springcloud.io.seata.server.session.SessionHolder;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:05
 * Version:V1.0
 */
public class DefaultCore implements Core {
    @Override
    public void setResourceManagerInbound(ResourceManagerInbound resourceManagerInbound) {

    }

    @Override
    public String begin(String applicationId, String transactionServiceGroup, String name, int timeout) throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession(applicationId, transactionServiceGroup, name,
                timeout);
        session.addSessionLifecycleListener(SessionHolder.getRootSessionManager());
        return null;
    }
}
