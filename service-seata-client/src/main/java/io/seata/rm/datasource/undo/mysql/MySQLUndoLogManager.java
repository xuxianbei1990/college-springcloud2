package io.seata.rm.datasource.undo.mysql;

import college.springcloud.io.seata.core.constants.ClientTableColumnsName;
import io.seata.rm.datasource.undo.AbstractUndoLogManager;

/** 我猜测这个是用来删除undoLog。而这个undoLog是从某个地方被拦截了。
 *  我猜测事务开始时候，会打标机，这时候会同步生成Undolog。而这些log
 *  被seata给拦截了
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 9:49
 * Version:V1.0
 */
public class MySQLUndoLogManager extends AbstractUndoLogManager {

    private static final String INSERT_UNDO_LOG_SQL = "INSERT INTO " + UNDO_LOG_TABLE_NAME +
            " (" + ClientTableColumnsName.UNDO_LOG_BRANCH_XID + ", " + ClientTableColumnsName.UNDO_LOG_XID + ", "
            + ClientTableColumnsName.UNDO_LOG_CONTEXT + ", " + ClientTableColumnsName.UNDO_LOG_ROLLBACK_INFO + ", "
            + ClientTableColumnsName.UNDO_LOG_LOG_STATUS + ", " + ClientTableColumnsName.UNDO_LOG_LOG_CREATED + ", "
            + ClientTableColumnsName.UNDO_LOG_LOG_MODIFIED + ")" +
            " VALUES (?, ?, ?, ?, ?, now(), now())";

    private static final String DELETE_UNDO_LOG_BY_CREATE_SQL = "DELETE FROM " + UNDO_LOG_TABLE_NAME +
            " WHERE log_created <= ? LIMIT ?";
}
