package college.seata.rm.datasource;

import college.seata.rm.AbstractResourceManager;
import college.springcloud.io.seata.common.Initialize;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.Resource;
import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 14:11
 * Version:V1.0
 */
@Data
public class DataSourceManager extends AbstractResourceManager implements Initialize {

    private Map<String, Resource> dataSourceCache = new ConcurrentHashMap<>();

    private ResourceManagerInbound asyncWorker;

    /**
     * 本来这个方法是通过SPI来初始化，我这里就简单调用下吧 。
     */
    @Override
    public void init() {
        AsyncWorker asyncWorker = new AsyncWorker();
        asyncWorker.init();
        initAsyncWorker(asyncWorker);
    }

    public synchronized void initAsyncWorker(ResourceManagerInbound asyncWorker) {
        setAsyncWorker(asyncWorker);
    }

    @Override
    public Map<String, Resource> getManagedResources() {
        return dataSourceCache;
    }

    public DataSourceProxy get(String resourceId) {
        return (DataSourceProxy)dataSourceCache.get(resourceId);
    }

    @Override
    public BranchType getBranchType() {
        return BranchType.AT;
    }

    @Override
    public void registerResource(Resource resource) {

    }

    @Override
    public BranchStatus branchCommit(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        return asyncWorker.branchCommit(branchType, xid, branchId, resourceId, applicationData);
    }
}
