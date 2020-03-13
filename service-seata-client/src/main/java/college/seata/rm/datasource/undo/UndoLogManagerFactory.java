package college.seata.rm.datasource.undo;

import college.springcloud.io.seata.core.exception.NotSupportYetException;
import com.alibaba.druid.util.JdbcConstants;
import college.seata.rm.datasource.undo.mysql.MySQLUndoLogManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 20:41
 * Version:V1.0
 */
public class UndoLogManagerFactory {

    private static final Map<String, UndoLogManager> UNDO_LOG_MANAGER_MAP = new HashMap<>();

    static {
        UNDO_LOG_MANAGER_MAP.put(JdbcConstants.MYSQL, new MySQLUndoLogManager());
        // 我这里只支持mysql
        //  UNDO_LOG_MANAGER_MAP.put(JdbcConstants.ORACLE, new OracleUndoLogManager());
    }

    private UndoLogManagerFactory() {}

    public static UndoLogManager getUndoLogManager(String dbType) {
        UndoLogManager undoLogManager = UNDO_LOG_MANAGER_MAP.get(dbType);
        if (undoLogManager == null) {
            throw new NotSupportYetException("not support dbType[" + dbType + "]");
        }
        return undoLogManager;
    }
}
