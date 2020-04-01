package college.seata.rm.datasource.undo;


import college.seata.rm.datasource.ConnectionProxy;
import college.seata.rm.datasource.DataSourceProxy;
import college.seata.rm.datasource.sql.struct.TableMeta;
import college.seata.rm.datasource.sql.struct.TableMetaCacheFactory;
import college.seata.spring.annotation.datasource.undo.BranchUndoLog;
import college.seata.spring.annotation.datasource.undo.UndoLogParser;
import college.seata.spring.annotation.datasource.undo.UndoLogParserFactory;
import college.springcloud.io.seata.common.util.BlobUtils;
import college.springcloud.io.seata.core.constants.ClientTableColumnsName;
import college.springcloud.io.seata.core.constants.ConfigurationKeys;
import college.springcloud.io.seata.core.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.*;

/**
 * undo_log 的数据操作，那么我的新问题就是：这个undo_log到底存了什么数据？
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 9:57
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractUndoLogManager implements UndoLogManager {

    protected static final String UNDO_LOG_TABLE_NAME = ConfigurationKeys.TRANSACTION_UNDO_LOG_DEFAULT_TABLE;

    private static final ThreadLocal<String> SERIALIZER_LOCAL = new ThreadLocal<>();

    protected static final String SELECT_UNDO_LOG_SQL = "SELECT * FROM " + UNDO_LOG_TABLE_NAME + " WHERE "
            + ClientTableColumnsName.UNDO_LOG_BRANCH_XID + " = ? AND " + ClientTableColumnsName.UNDO_LOG_XID
            + " = ? FOR UPDATE";

    protected static final String DELETE_UNDO_LOG_SQL = "DELETE FROM " + UNDO_LOG_TABLE_NAME + " WHERE "
            + ClientTableColumnsName.UNDO_LOG_BRANCH_XID + " = ? AND " + ClientTableColumnsName.UNDO_LOG_XID + " = ?";

    protected enum State {
        /**
         * This state can be properly rolled back by services
         */
        Normal(0),
        /**
         * This state prevents the branch transaction from inserting undo_log after the global transaction is rolled
         * back.
         */
        GlobalFinished(1);

        private int value;

        State(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 这里我可以改的简单点，只支持mysql即可，不用这么抽象
     * @param xids the xid set collections
     * @param branchIds the branch id set collections
     * @param conn the connection
     * @throws SQLException
     */
    @Override
    public void batchDeleteUndoLog(Set<String> xids, Set<Long> branchIds, Connection conn) throws SQLException {
        if (CollectionUtils.isEmpty(xids) || CollectionUtils.isEmpty(branchIds)) {
            return;
        }

        int xidSize = xids.size();
        int branchIdSize = branchIds.size();
        String batchDeleteSql = toBatchDeleteUndoLogSql(xidSize, branchIdSize);

        PreparedStatement deletePST = null;
        try {
            deletePST = conn.prepareStatement(batchDeleteSql);
            int paramsIndex = 1;
            for (Long branchId : branchIds) {
                deletePST.setLong(paramsIndex++, branchId);
            }
            for (String xid : xids) {
                deletePST.setString(paramsIndex++, xid);
            }
            int deleteRows = deletePST.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("batch delete undo log size {}", deleteRows);
            }
        } catch (Exception e) {
            if (!(e instanceof SQLException)) {
                e = new SQLException(e);
            }
            throw (SQLException)e;
        } finally {
            if (deletePST != null) {
                deletePST.close();
            }
        }

    }

    @Override
    public void flushUndoLogs(ConnectionProxy cp) throws SQLException {
        //下面代码其实就是插入数据undolog
//        ConnectionContext connectionContext = cp.getContext();
//        String xid = connectionContext.getXid();
//        long branchID = connectionContext.getBranchId();
//
//        BranchUndoLog branchUndoLog = new BranchUndoLog();
//        branchUndoLog.setXid(xid);
//        branchUndoLog.setBranchId(branchID);
//        branchUndoLog.setSqlUndoLogs(connectionContext.getUndoItems());
//
//        UndoLogParser parser = UndoLogParserFactory.getInstance();
//        byte[] undoLogContent = parser.encode(branchUndoLog);
//
//        if (log.isDebugEnabled()) {
//            log.debug("Flushing UNDO LOG: {}", new String(undoLogContent, Constants.DEFAULT_CHARSET));
//        }
//
//        insertUndoLogWithNormal(xid, branchID, buildContext(parser.getName()), undoLogContent,
//                cp.getTargetConnection());
    }

    @Override
    public void undo(DataSourceProxy dataSourceProxy, String xid, long branchId) throws TransactionException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement selectPST = null;
        boolean originalAutoCommit = true;

        for (; ; ) {
            try {
                conn = dataSourceProxy.getPlainConnection();

                // The entire undo process should run in a local transaction.
                if (originalAutoCommit = conn.getAutoCommit()) {
                    conn.setAutoCommit(false);
                }

                // Find UNDO LOG
                selectPST = conn.prepareStatement(SELECT_UNDO_LOG_SQL);
                selectPST.setLong(1, branchId);
                selectPST.setString(2, xid);
                rs = selectPST.executeQuery();

                boolean exists = false;
                while (rs.next()) {
                    exists = true;

                    // It is possible that the server repeatedly sends a rollback request to roll back
                    // the same branch transaction to multiple processes,
                    // ensuring that only the undo_log in the normal state is processed.
                    int state = rs.getInt(ClientTableColumnsName.UNDO_LOG_LOG_STATUS);
                    if (!canUndo(state)) {
                        if (log.isInfoEnabled()) {
                            log.info("xid {} branch {}, ignore {} undo_log", xid, branchId, state);
                        }
                        return;
                    }

                    String contextString = rs.getString(ClientTableColumnsName.UNDO_LOG_CONTEXT);
                    //serializer=jackson
                    Map<String, String> context = new HashMap<>();
                    Blob b = rs.getBlob(ClientTableColumnsName.UNDO_LOG_ROLLBACK_INFO);
                    byte[] rollbackInfo = BlobUtils.blob2Bytes(b);

                    String serializer = "jackson";
                    UndoLogParser parser = UndoLogParserFactory.getInstance();
                    BranchUndoLog branchUndoLog = parser.decode(rollbackInfo);

                    try {
                        // put serializer name to local
//                        setCurrentSerializer(parser.getName());
                        List<SQLUndoLog> sqlUndoLogs = branchUndoLog.getSqlUndoLogs();
                        if (sqlUndoLogs.size() > 1) {
                            Collections.reverse(sqlUndoLogs);
                        }
                        for (SQLUndoLog sqlUndoLog : sqlUndoLogs) {
                            TableMeta tableMeta = TableMetaCacheFactory.getTableMetaCache(dataSourceProxy).getTableMeta(
                                    conn, sqlUndoLog.getTableName(),dataSourceProxy.getResourceId());
                            sqlUndoLog.setTableMeta(tableMeta);
                            AbstractUndoExecutor undoExecutor = UndoExecutorFactory.getUndoExecutor(
                                    dataSourceProxy.getDbType(), sqlUndoLog);
                            undoExecutor.executeOn(conn);
                        }
                    } finally {
                        // remove serializer name
                        removeCurrentSerializer();
                    }
                }

                // If undo_log exists, it means that the branch transaction has completed the first phase,
                // we can directly roll back and clean the undo_log
                // Otherwise, it indicates that there is an exception in the branch transaction,
                // causing undo_log not to be written to the database.
                // For example, the business processing timeout, the global transaction is the initiator rolls back.
                // To ensure data consistency, we can insert an undo_log with GlobalFinished state
                // to prevent the local transaction of the first phase of other programs from being correctly submitted.
                // See https://github.com/seata/seata/issues/489

                if (exists) {
                    deleteUndoLog(xid, branchId, conn);
                    conn.commit();
                    if (log.isInfoEnabled()) {
                        log.info("xid {} branch {}, undo_log deleted with {}", xid, branchId,
                                State.GlobalFinished.name());
                    }
                } else {
//                    insertUndoLogWithGlobalFinished(xid, branchId, UndoLogParserFactory.getInstance(), conn);
                    conn.commit();
                    if (log.isInfoEnabled()) {
                        log.info("xid {} branch {}, undo_log added with {}", xid, branchId,
                                State.GlobalFinished.name());
                    }
                }

                return;
            } catch (SQLIntegrityConstraintViolationException e) {
                // Possible undo_log has been inserted into the database by other processes, retrying rollback undo_log
                if (log.isInfoEnabled()) {
                    log.info("xid {} branch {}, undo_log inserted, retry rollback", xid, branchId);
                }
            } catch (Throwable e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException rollbackEx) {
                        log.warn("Failed to close JDBC resource while undo ... ", rollbackEx);
                    }
                }
//                throw new BranchTransactionException(BranchRollbackFailed_Retriable, String
//                        .format("Branch session rollback failed and try again later xid = %s branchId = %s %s", xid,
//                                branchId, e.getMessage()), e);

            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (selectPST != null) {
                        selectPST.close();
                    }
                    if (conn != null) {
                        if (originalAutoCommit) {
                            conn.setAutoCommit(true);
                        }
                        conn.close();
                    }
                } catch (SQLException closeEx) {
                    log.warn("Failed to close JDBC resource while undo ... ", closeEx);
                }
            }
        }
    }

    protected static boolean canUndo(int state) {
        return state == State.Normal.getValue();
    }


    protected abstract void insertUndoLogWithNormal(String xid, long branchId, String rollbackCtx,
                                                    byte[] undoLogContent, Connection conn) throws SQLException;


    @Override
    public void deleteUndoLog(String xid, long branchId, Connection conn) throws SQLException {
        PreparedStatement deletePST = null;
        try {
            deletePST = conn.prepareStatement(DELETE_UNDO_LOG_SQL);
            deletePST.setLong(1, branchId);
            deletePST.setString(2, xid);
            deletePST.executeUpdate();
        } catch (Exception e) {
            if (!(e instanceof SQLException)) {
                e = new SQLException(e);
            }
            throw (SQLException)e;
        } finally {
            if (deletePST != null) {
                deletePST.close();
            }
        }
    }

    protected static String toBatchDeleteUndoLogSql(int xidSize, int branchIdSize) {
        StringBuilder sqlBuilder = new StringBuilder(64);
        sqlBuilder.append("DELETE FROM ").append(UNDO_LOG_TABLE_NAME).append(" WHERE  ").append(
                ClientTableColumnsName.UNDO_LOG_BRANCH_XID).append(" IN ");
        appendInParam(branchIdSize, sqlBuilder);
        sqlBuilder.append(" AND ").append(ClientTableColumnsName.UNDO_LOG_XID).append(" IN ");
        appendInParam(xidSize, sqlBuilder);
        return sqlBuilder.toString();
    }

    protected static void appendInParam(int size, StringBuilder sqlBuilder) {
        sqlBuilder.append(" (");
        for (int i = 0; i < size; i++) {
            sqlBuilder.append("?");
            if (i < (size - 1)) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(") ");
    }

    public static void removeCurrentSerializer() {
        SERIALIZER_LOCAL.remove();
    }
}
