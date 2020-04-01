package college.seata.rm.datasource.undo.mysql;

import college.seata.rm.datasource.undo.AbstractUndoLogManager;
import college.springcloud.io.seata.common.util.BlobUtils;
import college.springcloud.io.seata.core.constants.ClientTableColumnsName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/** 我猜测这个是用来删除undoLog。而这个undoLog是从某个地方被拦截了。
 *  我猜测事务开始时候，会打标机，这时候会同步生成Undolog。而这些log
 *  被seata给拦截了
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 9:49
 * Version:V1.0
 */
public class MySQLUndoLogManager extends AbstractUndoLogManager {

    //undo_log
    private static final String INSERT_UNDO_LOG_SQL = "INSERT INTO " + UNDO_LOG_TABLE_NAME +
            " (" + ClientTableColumnsName.UNDO_LOG_BRANCH_XID + ", " + ClientTableColumnsName.UNDO_LOG_XID + ", "
            + ClientTableColumnsName.UNDO_LOG_CONTEXT + ", " + ClientTableColumnsName.UNDO_LOG_ROLLBACK_INFO + ", "
            + ClientTableColumnsName.UNDO_LOG_LOG_STATUS + ", " + ClientTableColumnsName.UNDO_LOG_LOG_CREATED + ", "
            + ClientTableColumnsName.UNDO_LOG_LOG_MODIFIED + ")" +
            " VALUES (?, ?, ?, ?, ?, now(), now())";

    private static final String DELETE_UNDO_LOG_BY_CREATE_SQL = "DELETE FROM " + UNDO_LOG_TABLE_NAME +
            " WHERE log_created <= ? LIMIT ?";

    @Override
    protected void insertUndoLogWithNormal(String xid, long branchID, String rollbackCtx,
                                           byte[] undoLogContent, Connection conn) throws SQLException {
        insertUndoLog(xid, branchID, rollbackCtx, undoLogContent, State.Normal, conn);
    }

    private void insertUndoLog(String xid, long branchID, String rollbackCtx,
                               byte[] undoLogContent, State state, Connection conn) throws SQLException {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(INSERT_UNDO_LOG_SQL);
            pst.setLong(1, branchID);
            pst.setString(2, xid);
            pst.setString(3, rollbackCtx);
            pst.setBlob(4, BlobUtils.bytes2Blob(undoLogContent));
            pst.setInt(5, state.getValue());
            pst.executeUpdate();
        } catch (Exception e) {
            if (!(e instanceof SQLException)) {
                e = new SQLException(e);
            }
            throw (SQLException) e;
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    @Override
    public int deleteUndoLogByLogCreated(Date logCreated, int limitRows, Connection conn) throws SQLException {
        return 0;
    }
}
