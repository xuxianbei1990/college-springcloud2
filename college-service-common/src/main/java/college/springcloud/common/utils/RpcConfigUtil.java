package college.springcloud.common.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import feign.Request;

import java.util.concurrent.TimeUnit;

/**
 * @author: xuxianbei
 * Date: 2020/12/19
 * Time: 15:31
 * Version:V1.0
 */
public class RpcConfigUtil {

    /**
     * 个人觉得这里使用ThreadLocal 就行了
     */
    private static ThreadLocal<Request.Options> optionsLocal = new TransmittableThreadLocal();

    public static void setOnceTimeByMilliseconds(int connectTimeout, int readTimeout) {
        Request.Options options = new Request.Options(connectTimeout, readTimeout, true);
        optionsLocal.set(options);
    }

    public static Request.Options getOptions() {
        return optionsLocal.get();
    }

    public static void remove() {

        if (optionsLocal.get() == null) {
            return;
        }
        optionsLocal.remove();
    }
}
