package college.seata.rm.datasource.undo;

import college.seata.rm.datasource.ConnectionProxy;
import college.seata.rm.datasource.DataSourceProxy;
import college.springcloud.io.seata.core.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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

    void undo(DataSourceProxy dataSourceProxy, String xid, long branchId) throws TransactionException;
    /**
     * delete undolog by created
     * @param logCreated the created time
     * @param limitRows the limit rows
     * @param conn the connection
     * @return the update rows
     * @throws SQLException the sql exception
     */
    int deleteUndoLogByLogCreated(Date logCreated, int limitRows, Connection conn) throws SQLException;

    /**
     * Delete undo log.
     *
     * @param xid      the xid
     * @param branchId the branch id
     * @param conn     the conn
     * @throws SQLException the sql exception
     */
    void deleteUndoLog(String xid, long branchId, Connection conn) throws SQLException;
}
