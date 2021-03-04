package college.springcloud.log.util.start;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author: xuxianbei
 * Date: 2021/3/3
 * Time: 16:58
 * Version:V1.0
 */
public class ApplicationContextUtil {

    private static final String DEV_PROFILE = "dev";
    private static final String PROD_PROFILE = "prod";
    private static final String SPRING_PROFILE_NAME = "spring.profiles.active";

    private static String PROFILE = DEV_PROFILE;

    public static boolean isProd() {
        return getProfile().equalsIgnoreCase(PROD_PROFILE);
    }

    private static ApplicationContext context;

    public static synchronized void setContext(ApplicationContext context) {
        ApplicationContextUtil.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static String getProfile() {
        if (PROFILE.isEmpty()) {
            synchronized (ApplicationContextUtil.class) {
                if (getContext() == null) {
                    return PROFILE;
                }
                Environment environment = getContext().getBean(Environment.class);
                PROFILE = environment.getProperty(SPRING_PROFILE_NAME, DEV_PROFILE);
            }

        }
        return PROFILE;
    }
}
