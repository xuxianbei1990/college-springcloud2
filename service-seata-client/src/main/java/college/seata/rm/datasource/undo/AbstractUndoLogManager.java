package college.seata.rm.datasource.undo;


import college.seata.rm.datasource.ConnectionProxy;
import college.springcloud.io.seata.core.constants.ClientTableColumnsName;
import college.springcloud.io.seata.core.constants.ConfigurationKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

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

    protected abstract void insertUndoLogWithNormal(String xid, long branchId, String rollbackCtx,
                                                    byte[] undoLogContent, Connection conn) throws SQLException;


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
}
