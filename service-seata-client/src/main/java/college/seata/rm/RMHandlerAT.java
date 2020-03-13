package college.seata.rm;

import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManager;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 15:24
 * Version:V1.0
 */
public class RMHandlerAT extends AbstractRMHandler {

    @Override
    protected ResourceManager getResourceManager() {
        return DefaultResourceManager.get().getResourceManager(BranchType.AT);
    }

    @Override
    public BranchType getBranchType() {
        return BranchType.AT;
    }
}
