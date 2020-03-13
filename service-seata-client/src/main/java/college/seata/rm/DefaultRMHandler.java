package college.seata.rm;

import college.springcloud.io.seata.core.exception.FrameworkException;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManager;
import college.springcloud.io.seata.core.protocol.transaction.BranchCommitRequest;
import college.springcloud.io.seata.core.protocol.transaction.BranchCommitResponse;
import college.seata.rm.tcc.RMHandlerTCC;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 16:01
 * Version:V1.0
 */
public class DefaultRMHandler extends AbstractRMHandler {

    protected static Map<BranchType, AbstractRMHandler> allRMHandlersMap
            = new ConcurrentHashMap();

    protected DefaultRMHandler() {
        initRMHandlers();
    }

    protected void initRMHandlers() {
        /**
         * 那么这里说白了也只是把 META-INF services 的 io.seata.rm.AbstractRMHandler  io.seata.rm.RMHandlerAT  io.seata.rm.tcc.RMHandlerTCC
         * 我这里简单处理，不通过类加载机制，EnhancedServiceLoader.loadAll(AbstractRMHandler.class);
         */
        List<AbstractRMHandler> allRMHandlers = new ArrayList<>();
        allRMHandlers.add(new RMHandlerAT());
        allRMHandlers.add(new RMHandlerTCC());
        if (CollectionUtils.isNotEmpty(allRMHandlers)) {
            for (AbstractRMHandler rmHandler : allRMHandlers) {
                allRMHandlersMap.put(rmHandler.getBranchType(), rmHandler);
            }
        }
    }

    public static AbstractRMHandler get() {
        return SingletomHolder.INSTANCE.get();
    }

    enum SingletomHolder {
        INSTANCE(new DefaultRMHandler());

        SingletomHolder(DefaultRMHandler rmHandler) {
            this.single = rmHandler;
        }

        private DefaultRMHandler single;

        public DefaultRMHandler get() {
            return single;
        }
    }

    @Override
    protected ResourceManager getResourceManager() {
        throw new RuntimeException("DefaultRMHandler isn't a real AbstractRMHandler");
    }

    @Override
    public BranchType getBranchType() {
        throw new FrameworkException("DefaultRMHandler isn't a real AbstractRMHandler");
    }

    @Override
    public BranchCommitResponse handle(BranchCommitRequest request) {
        return getRMHandler(request.getBranchType()).handle(request);
    }

    protected AbstractRMHandler getRMHandler(BranchType branchType) {
        return allRMHandlersMap.get(branchType);
    }
}
