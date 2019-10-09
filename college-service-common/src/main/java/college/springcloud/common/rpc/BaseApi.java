package college.springcloud.common.rpc;

import college.springcloud.common.service.IService;
import college.springcloud.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 16:56
 * Version:V1.0
 */
public abstract class BaseApi<T> implements Api<T> {
    @Autowired
    protected IService<T> service;

    @Override
    public Result<String> sayHello(String name) {
        return Result.success("Hello " + name);
    }

    @Override
    public Result<List<T>> queryAll() {
        return Result.success(service.queryAll());
    }
}
