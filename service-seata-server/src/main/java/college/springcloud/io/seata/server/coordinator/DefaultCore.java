package college.springcloud.io.seata.server.coordinator;

import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.GlobalStatus;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import college.springcloud.io.seata.server.event.EventBus;
import college.springcloud.io.seata.server.event.EventBusManager;
import college.springcloud.io.seata.server.session.GlobalSession;
import college.springcloud.io.seata.server.session.SessionHolder;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:05
 * Version:V1.0
 */
public class DefaultCore implements Core {

    private EventBus eventBus = EventBusManager.get();

    @Override
    public void setResourceManagerInbound(ResourceManagerInbound resourceManagerInbound) {

    }

    @Override
    public String begin(String applicationId, String transactionServiceGroup, String name, int timeout) throws TransactionException {
        GlobalSession session = GlobalSession.createGlobalSession(applicationId, transactionServiceGroup, name,
                timeout);
        session.addSessionLifecycleListener(SessionHolder.getRootSessionManager());
        session.begin();

        //好像只是用来统计的，没干啥的
//        eventBus.post(new GlobalTransactionEvent(session.getTransactionId(), GlobalTransactionEvent.ROLE_TC,
//                session.getTransactionName(), session.getBeginTime(), null, session.getStatus()));

        return session.getXid();
    }

    @Override
    public GlobalStatus commit(String xid) throws TransactionException {
        return null;
    }
}
