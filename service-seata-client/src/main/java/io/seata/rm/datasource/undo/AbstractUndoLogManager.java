package io.seata.rm.datasource.undo;


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
