/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package college.seata.rm.datasource;

import college.seata.rm.DefaultResourceManager;
import college.seata.rm.datasource.exec.LockConflictException;
import college.seata.rm.datasource.exec.LockRetryController;
import college.seata.rm.datasource.sql.SQLRecognizer;
import college.seata.rm.datasource.sql.SQLVisitorFactory;
import college.seata.rm.datasource.undo.SQLUndoLog;
import college.seata.rm.datasource.undo.UndoLogManagerFactory;
import college.springcloud.io.seata.core.context.RootContext;
import college.springcloud.io.seata.core.exception.TransactionException;
import college.springcloud.io.seata.core.model.BranchStatus;
import college.springcloud.io.seata.core.model.BranchType;
import lombok.Data;

import java.sql.*;
import java.util.concurrent.Callable;


/**
 * 数据库操作全部在这里
 * <p>
 * <p>
 * The type Connection proxy.
 *
 * @author sharajava
 */
@Data
public class ConnectionProxy extends AbstractConnectionProxy {


    private ConnectionContext context = new ConnectionContext();
    private static final int REPORT_RETRY_COUNT = 5;

    /**
     * Instantiates a new Abstract connection proxy.
     *
     * @param dataSourceProxy  the data source proxy
     * @param targetConnection the target connection
     */
    public ConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection) {
        super(dataSourceProxy, targetConnection);
    }

    //我现在要做的是了解主体思路，然后了解细节
    public void bind(String xid) {
        context.setXid(xid);
    }


    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

    }

    @Override
    public void commit() throws SQLException {
        if (context.getXid() != null) {
            processGlobalTransactionCommit();
        } else {
            targetConnection.commit();
        }
    }

    private void processGlobalTransactionCommit() throws SQLException {
        UndoLogManagerFactory.getUndoLogManager(this.getDbType()).flushUndoLogs(this);
        targetConnection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        targetConnection.rollback();
        if (context.inGlobalTransaction()) {
            if (context.isBranchRegistered()) {
                report(false);
            }
        }
        context.reset();
    }

    private void report(boolean commitDone) throws SQLException {
        int retry = REPORT_RETRY_COUNT;
        while (retry > 0) {
            try {
                DefaultResourceManager.get().branchReport(BranchType.AT, context.getXid(), context.getBranchId(),
                        commitDone ? BranchStatus.PhaseOne_Done : BranchStatus.PhaseOne_Failed, null);
            } catch (TransactionException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement targetStatement = getTargetConnection().createStatement();
        return new StatementProxy(this, targetStatement);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        String dbType = getDbType();
        // support oracle 10.2+
        PreparedStatement targetPreparedStatement = null;
        if (RootContext.inGlobalTransaction()) {
            SQLRecognizer sqlRecognizer = SQLVisitorFactory.get(sql, dbType);
        }
        return null;
    }

    public void appendLockKey(String lockKey) {

    }

    public void appendUndoLog(SQLUndoLog sqlUndoLog) {

    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return null;
    }


    public static class LockRetryPolicy {
        protected final static boolean LOCK_RETRY_POLICY_BRANCH_ROLLBACK_ON_CONFLICT = true;

        public <T> T execute(Callable<T> callable) throws Exception {
            return callable.call();
        }

        protected <T> T doRetryOnLockConflict(Callable<T> callable) throws Exception {
            LockRetryController lockRetryController = new LockRetryController();
            while (true) {
                try {
                    return callable.call();
                } catch (LockConflictException lockConflict) {
                    onException(lockConflict);
                    lockRetryController.sleep(lockConflict);
                } catch (Exception e) {
                    onException(e);
                    throw e;
                }
            }
        }

        /**
         * Callback on exception in doLockRetryOnConflict.
         *
         * @param e invocation exception
         * @throws Exception error
         */
        protected void onException(Exception e) throws Exception {
        }
    }
}
