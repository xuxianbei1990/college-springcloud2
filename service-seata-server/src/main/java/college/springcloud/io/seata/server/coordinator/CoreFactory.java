package college.springcloud.io.seata.server.coordinator;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 17:04
 * Version:V1.0
 */
public class CoreFactory {

    private static class SingletonHolder {
        private static Core INSTANCE = new DefaultCore();
    }

    public static final Core get() {
        return SingletonHolder.INSTANCE;
    }
}
