package college.seata.rm;

import college.seata.rm.datasource.DataSourceManager;
import college.seata.rm.datasource.DataSourceProxy;
import college.seata.rm.datasource.undo.UndoLogManagerFactory;
import college.springcloud.io.seata.core.model.BranchType;
import college.springcloud.io.seata.core.model.ResourceManager;
import college.springcloud.io.seata.core.protocol.transaction.UndoLogDeleteRequest;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static college.springcloud.io.seata.core.protocol.transaction.UndoLogDeleteRequest.DEFAULT_SAVE_DAYS;

/**
 * @author: xuxianbei
 * Date: 2020/3/11
 * Time: 15:24
 * Version:V1.0
 */
@Slf4j
public class RMHandlerAT extends AbstractRMHandler {

    private static final int LIMIT_ROWS = 3000;

    @Override
    protected ResourceManager getResourceManager() {
        return DefaultResourceManager.get().getResourceManager(BranchType.AT);
    }

    @Override
    public BranchType getBranchType() {
        return BranchType.AT;
    }

    @Override
    public void handle(UndoLogDeleteRequest request) {
        DataSourceManager dataSourceManager = (DataSourceManager)getResourceManager();
        DataSourceProxy dataSourceProxy = dataSourceManager.get(request.getResourceId());
        if (dataSourceProxy == null) {
            log.warn("Failed to get dataSourceProxy for delete undolog on " + request.getResourceId());
            return;
        }
        Date logCreatedSave = getLogCreated(request.getSaveDays());
        Connection conn = null;
        try {
            conn = dataSourceProxy.getPlainConnection();
            int deleteRows = 0;
            do {
                try {
                    deleteRows = UndoLogManagerFactory.getUndoLogManager(dataSourceProxy.getDbType())
                            .deleteUndoLogByLogCreated(logCreatedSave, LIMIT_ROWS, conn);
                    if (deleteRows > 0 && !conn.getAutoCommit()) {
                        conn.commit();
                    }
                } catch (SQLException exx) {
                    if (deleteRows > 0 && !conn.getAutoCommit()) {
                        conn.rollback();
                    }
                    throw exx;
                }
            } while (deleteRows == LIMIT_ROWS);
        } catch (Exception e) {
            log.error("Failed to delete expired undo_log, error:{}", e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.warn("Failed to close JDBC resource while deleting undo_log ", closeEx);
                }
            }
        }
    }

    private Date getLogCreated(int saveDays) {
        if (saveDays <= 0) {
            saveDays = DEFAULT_SAVE_DAYS;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0 - saveDays);
        return calendar.getTime();
    }
}
