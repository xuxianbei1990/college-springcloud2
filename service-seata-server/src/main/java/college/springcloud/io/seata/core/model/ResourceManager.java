package college.springcloud.io.seata.core.model;

import java.util.Map;

/** 好像是用来和数据库打交道的。
 * @author: xuxianbei
 * Date: 2020/3/9
 * Time: 14:54
 * Version:V1.0
 * 总的管理接口
 */
public interface ResourceManager extends ResourceManagerInbound {

    Map<String, Resource> getManagedResources();

    BranchType getBranchType();

    void registerResource(Resource resource);
}
