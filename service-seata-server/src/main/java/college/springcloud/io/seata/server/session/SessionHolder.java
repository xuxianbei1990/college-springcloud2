package college.springcloud.io.seata.server.session;

import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;

/**
 * @author: xuxianbei
 * Date: 2020/3/13
 * Time: 18:20
 * Version:V1.0
 */
public class SessionHolder {

    private static SessionManager ROOT_SESSION_MANAGER = new DefaultSessionManager();


    public static final SessionManager getRootSessionManager() {
        if (ROOT_SESSION_MANAGER == null) {
            throw new ShouldNeverHappenException("SessionManager is NOT init!");
        }
        return ROOT_SESSION_MANAGER;
    }
}
