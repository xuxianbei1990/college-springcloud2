package college.springcloud.io.seata.server.coordinator;

import college.springcloud.io.seata.core.model.ResourceManagerInbound;
import college.springcloud.io.seata.core.model.TransactionManager;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:04
 * Version:V1.0
 */
public interface Core extends TransactionManager {

    void setResourceManagerInbound(ResourceManagerInbound resourceManagerInbound);

}
