package college.springcloud.common.memorycopy;

/**
 * 场景：一个微服务。主库插入数据，从库读取数据，因为主从的问题导致数据延迟。需要写大量的同步数据
 *   多个微服务呢？不用处理
 *   拦截insert，update。select从库读取，然后内存更新。
 * @author: xuxianbei
 * Date: 2020/3/30
 * Time: 20:08
 * Version:V1.0
 */
public class Design {
}
