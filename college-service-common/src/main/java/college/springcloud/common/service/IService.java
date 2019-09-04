package college.springcloud.common.service;

import java.util.List;

/**
 * User: EDZ
 * Date: 2019/8/27
 * Time: 17:48
 * Version:V1.0
 */
public interface IService<T> {
    List<T> queryAll();
}
