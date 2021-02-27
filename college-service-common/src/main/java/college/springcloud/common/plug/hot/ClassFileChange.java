package college.springcloud.common.plug.hot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author: xuxianbei
 * Date: 2021/2/24
 * Time: 10:25
 * Version:V1.0
 */
public class ClassFileChange extends Thread {

    private String[] paths;
    private Map<String, Long>  fileLastLoadTimeMap = new ConcurrentHashMap<>();
    private Function task;

    {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.interrupt()));
    }

    public ClassFileChange(Function task, String... paths) {

    }
}
