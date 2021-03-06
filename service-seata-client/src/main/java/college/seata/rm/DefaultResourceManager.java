package college.seata.rm;

import college.springcloud.io.seata.common.loader.EnhancedServiceLoader;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.Resource;
import college.springcloud.io.seata.core.model.ResourceManager;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 14:56
 * Version:V1.0
 */
public class DefaultResourceManager implements ResourceManager {

    protected static Map<BranchType, ResourceManager> resourceManagers
            = new ConcurrentHashMap<>();

    private DefaultResourceManager() {
        initResourceManagers();
    }

    protected void initResourceManagers() {
        //init all resource managers 说白了就是一个类加载器  DataSourceManager   TCCResourceManager
        /**
         * io.seata.rm.datasource.DataSourceManager
         * io.seata.rm.tcc.TCCResourceManager
         */
        List<ResourceManager> allResourceManagers = EnhancedServiceLoader.loadAll(ResourceManager.class);
        if (CollectionUtils.isNotEmpty(allResourceManagers)) {
            for (ResourceManager rm : allResourceManagers) {
                resourceManagers.put(rm.getBranchType(), rm);
            }
        }
    }

    public static DefaultResourceManager get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void registerResource(Resource resource) {
        getResourceManager(resource.getBranchType()).registerResource(resource);
    }

    @Override
    public Map<String, Resource> getManagedResources() {
        Map<String, Resource> allResource = new HashMap<>();
        for (ResourceManager rm : resourceManagers.values()) {
            Map<String, Resource> tempResources = rm.getManagedResources();
            if (tempResources != null) {
                allResource.putAll(tempResources);
            }
        }
        return allResource;
    }

    @Override
    public BranchType getBranchType() {
        throw new RuntimeException("DefaultResourceManager isn't a real ResourceManager");
    }

    private static class SingletonHolder {
        private static DefaultResourceManager INSTANCE = new DefaultResourceManager();
    }

    @Override
    public BranchStatus branchCommit(BranchType branchType, String xid, long branchId,
                                     String resourceId, String applicationData)
            throws TransactionException {
        return getResourceManager(branchType).branchCommit(branchType, xid, branchId, resourceId, applicationData);
    }

    @Override
    public BranchStatus branchRollback(BranchType branchType, String xid, long branchId, String resourceId, String applicationData) throws TransactionException {
        return null;
    }

    public ResourceManager getResourceManager(BranchType branchType) {
        ResourceManager rm = resourceManagers.get(branchType);
        if (rm == null) {
            throw new RuntimeException("No ResourceManager for BranchType:" + branchType.name());
        }
        return rm;
    }

    @Override
    public void branchReport(BranchType branchType, String xid, long branchId, BranchStatus status,
                             String applicationData) throws TransactionException {
        getResourceManager(branchType).branchReport(branchType, xid, branchId, status, applicationData);
    }

}
