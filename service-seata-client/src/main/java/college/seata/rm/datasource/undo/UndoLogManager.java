package college.seata.rm.datasource.undo;

import college.seata.rm.datasource.ConnectionProxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 9:47
 * Version:V1.0
 */
public interface UndoLogManager {

    /**
     * batch Delete undo log.
     *
     * @param xids the xid set collections
     * @param branchIds the branch id set collections
     * @param conn the connection
     * @throws SQLException the sql exception
     */
    void batchDeleteUndoLog(Set<String> xids, Set<Long> branchIds, Connection conn) throws SQLException;

    void flushUndoLogs(ConnectionProxy cp) throws SQLException;
}
