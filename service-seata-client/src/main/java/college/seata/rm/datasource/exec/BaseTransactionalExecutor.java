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
package college.seata.rm.datasource.exec;


import college.seata.rm.datasource.ColumnUtils;
import college.seata.rm.datasource.ConnectionProxy;
import college.seata.rm.datasource.StatementProxy;
import college.seata.rm.datasource.sql.SQLRecognizer;
import college.seata.rm.datasource.sql.SQLType;
import college.seata.rm.datasource.sql.struct.Field;
import college.seata.rm.datasource.sql.struct.TableMeta;
import college.seata.rm.datasource.sql.struct.TableMetaCacheFactory;
import college.seata.rm.datasource.sql.struct.TableRecords;
import college.seata.rm.datasource.undo.SQLUndoLog;
import college.springcloud.io.seata.core.context.RootContext;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * The type Base transactional executor.
 *
 * @author sharajava
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public abstract class BaseTransactionalExecutor<T, S extends Statement> implements Executor {

    /**
     * The Statement proxy.
     */
    protected StatementProxy<S> statementProxy;

    /**
     * The Statement callback.
     */
    protected StatementCallback<T, S> statementCallback;

    /**
     * The Sql recognizer.
     */
    protected SQLRecognizer sqlRecognizer;

    private TableMeta tableMeta;

    /**
     * Instantiates a new Base transactional executor.
     *
     * @param statementProxy    the statement proxy
     * @param statementCallback the statement callback
     * @param sqlRecognizer     the sql recognizer
     */
    public BaseTransactionalExecutor(StatementProxy<S> statementProxy, StatementCallback<T, S> statementCallback,
                                     SQLRecognizer sqlRecognizer) {
        this.statementProxy = statementProxy;
        this.statementCallback = statementCallback;
        this.sqlRecognizer = sqlRecognizer;
    }

    @Override
    public Object execute(Object... args) throws Throwable {
        if (RootContext.inGlobalTransaction()) {
            String xid = RootContext.getXID();
            //在执行的时候必须绑定这个，这样这个语句才归属于这个seata 全局事务
            statementProxy.getConnectionProxy().bind(xid);
        }
        return doExecute(args);
    }

    /**
     * Do execute object.
     *
     * @param args the args
     * @return the object
     * @throws Throwable the throwable
     */
    protected abstract Object doExecute(Object... args) throws Throwable;



    /**
     * Gets from table in sql.
     *
     * @return the from table in sql
     */
    protected String getFromTableInSQL() {
        String tableName = sqlRecognizer.getTableName();
        String tableAlias = sqlRecognizer.getTableAlias();
        return tableAlias == null ? tableName : tableName + " " + tableAlias;
    }

    /**
     * Gets table meta.
     *
     * @return the table meta
     */
    protected TableMeta getTableMeta() {
        return getTableMeta(sqlRecognizer.getTableName());
    }

    /**
     * Gets table meta.
     *
     * @param tableName the table name
     * @return the table meta
     */
    protected TableMeta getTableMeta(String tableName) {
        if (tableMeta != null) {
            return tableMeta;
        }
        ConnectionProxy connectionProxy = statementProxy.getConnectionProxy();
        tableMeta = TableMetaCacheFactory.getTableMetaCache(connectionProxy.getDbType())
                .getTableMeta(connectionProxy.getTargetConnection(), tableName,connectionProxy.getDataSourceProxy().getResourceId());
        return tableMeta;
    }

    /**
     * the columns contains table meta pk
     * @param columns the column name list
     * @return true: contains pk false: not contains pk
     */
    protected boolean containsPK(List<String> columns) {
        if (columns == null || columns.isEmpty()) {
            return false;
        }
        List<String> newColumns = ColumnUtils.delEscape(columns, getDbType());
        return getTableMeta().containsPK(newColumns);
    }

    /**
     * compare column name and primary key name
     * @param columnName the primary key column name
     * @return true: equal false: not equal
     */
    protected boolean equalsPK(String columnName) {
        String newColumnName = ColumnUtils.delEscape(columnName, getDbType());
        return StringUtils.equalsIgnoreCase(getTableMeta().getPkName(), newColumnName);
    }

    /**
     * prepare undo log.
     *
     * @param beforeImage the before image
     * @param afterImage  the after image
     * @throws SQLException the sql exception
     */
    protected void prepareUndoLog(TableRecords beforeImage, TableRecords afterImage) throws SQLException {
        if (beforeImage.getRows().size() == 0 && afterImage.getRows().size() == 0) {
            return;
        }

        ConnectionProxy connectionProxy = statementProxy.getConnectionProxy();

        TableRecords lockKeyRecords = sqlRecognizer.getSQLType() == SQLType.DELETE ? beforeImage : afterImage;
        String lockKeys = buildLockKey(lockKeyRecords);
        connectionProxy.appendLockKey(lockKeys);

        SQLUndoLog sqlUndoLog = buildUndoItem(beforeImage, afterImage);
        connectionProxy.appendUndoLog(sqlUndoLog);
    }

    /**
     * build lockKey
     *
     * @param rowsIncludingPK the records
     * @return the string
     */
    protected String buildLockKey(TableRecords rowsIncludingPK) {
        if (rowsIncludingPK.size() == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(rowsIncludingPK.getTableMeta().getTableName());
        sb.append(":");
        int filedSequence = 0;
        List<Field> pkRows = rowsIncludingPK.pkRows();
        for (Field field : pkRows) {
            sb.append(field.getValue());
            filedSequence++;
            if (filedSequence < pkRows.size()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * build a SQLUndoLog
     *
     * @param beforeImage the before image
     * @param afterImage  the after image
     * @return sql undo log
     */
    protected SQLUndoLog buildUndoItem(TableRecords beforeImage, TableRecords afterImage) {
        SQLType sqlType = sqlRecognizer.getSQLType();
        String tableName = sqlRecognizer.getTableName();

        SQLUndoLog sqlUndoLog = new SQLUndoLog();
        sqlUndoLog.setSqlType(sqlType);
        sqlUndoLog.setTableName(tableName);
        sqlUndoLog.setBeforeImage(beforeImage);
        sqlUndoLog.setAfterImage(afterImage);
        return sqlUndoLog;
    }


    /**
     * build a BeforeImage
     *
     * @param tableMeta         the tableMeta
     * @param selectSQL         the selectSQL
     * @param paramAppenderList the paramAppender list
     * @return a tableRecords
     * @throws SQLException the sql exception
     */
    protected TableRecords buildTableRecords(TableMeta tableMeta, String selectSQL, ArrayList<List<Object>> paramAppenderList) throws SQLException {
        TableRecords tableRecords = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            if (paramAppenderList.isEmpty()) {
                st = statementProxy.getConnection().createStatement();
                rs = st.executeQuery(selectSQL);
            } else {
                if (paramAppenderList.size() == 1) {
                    ps = statementProxy.getConnection().prepareStatement(selectSQL);
                    List<Object> paramAppender = paramAppenderList.get(0);
                    for (int i = 0; i < paramAppender.size(); i++) {
                        ps.setObject(i + 1, paramAppender.get(i));
                    }
                } else {
                    ps = statementProxy.getConnection().prepareStatement(selectSQL);
                    List<Object> paramAppender = null;
                    for (int i = 0; i < paramAppenderList.size(); i++) {
                        paramAppender = paramAppenderList.get(i);
                        for (int j = 0; j < paramAppender.size(); j++) {
                            ps.setObject(i * paramAppender.size() + j + 1, paramAppender.get(j));
                        }
                    }
                }
                rs = ps.executeQuery();
            }
            tableRecords = TableRecords.buildRecords(tableMeta, rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return tableRecords;
    }

    /**
     * build TableRecords
     *
     * @param pkValues the pkValues
     * @return return TableRecords;
     * @throws SQLException
     */
    protected TableRecords buildTableRecords(List<Object> pkValues) throws SQLException {
        TableRecords afterImage;
        String pk = getTableMeta().getPkName();
        StringJoiner pkValuesJoiner = new StringJoiner(" , ",
            "SELECT * FROM " + getFromTableInSQL() + " WHERE " + pk + " in (", ")");
        for (Object pkValue : pkValues) {
            pkValuesJoiner.add("?");
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = statementProxy.getConnection().prepareStatement(pkValuesJoiner.toString());

            for (int i = 1; i <= pkValues.size(); i++) {
                ps.setObject(i, pkValues.get(i - 1));
            }

            rs = ps.executeQuery();
            afterImage = TableRecords.buildRecords(getTableMeta(), rs);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return afterImage;
    }

    /**
     * get db type
     * @return
     */
    protected String getDbType() {
        return statementProxy.getConnectionProxy().getDbType();
    }

}
