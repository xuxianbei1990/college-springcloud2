package college.seata.rm.datasource.undo;

import college.seata.rm.datasource.sql.struct.Field;
import college.seata.rm.datasource.sql.struct.KeyType;
import college.seata.rm.datasource.sql.struct.Row;
import college.seata.rm.datasource.sql.struct.TableRecords;
import lombok.extern.slf4j.Slf4j;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author: xuxianbei
 * Date: 2020/3/31
 * Time: 10:49
 * Version:V1.0
 */
@Slf4j
public abstract class AbstractUndoExecutor {

    /**
     * The Sql undo log.
     */
    protected SQLUndoLog sqlUndoLog;
    public static final boolean IS_UNDO_DATA_VALIDATION_ENABLE = true;

    public void executeOn(Connection conn) throws SQLException {

        if (IS_UNDO_DATA_VALIDATION_ENABLE && !dataValidationAndGoOn(conn)) {
            return;
        }

        try {
            String undoSQL = buildUndoSQL();

            PreparedStatement undoPST = conn.prepareStatement(undoSQL);

            TableRecords undoRows = getUndoRows();

            for (Row undoRow : undoRows.getRows()) {
                ArrayList<Field> undoValues = new ArrayList<>();
                Field pkValue = null;
                for (Field field : undoRow.getFields()) {
                    if (field.getKeyType() == KeyType.PrimaryKey) {
                        pkValue = field;
                    } else {
                        undoValues.add(field);
                    }
                }

                undoPrepare(undoPST, undoValues, pkValue);

                undoPST.executeUpdate();
            }

        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            } else {
                throw new SQLException(ex);
            }

        }

    }

    protected abstract String buildUndoSQL();

    protected abstract TableRecords getUndoRows();

    protected void undoPrepare(PreparedStatement undoPST, ArrayList<Field> undoValues, Field pkValue)
            throws SQLException {
        int undoIndex = 0;
        for (Field undoValue : undoValues) {
            undoIndex++;
            if (undoValue.getType() == JDBCType.BLOB.getVendorTypeNumber()) {
                SerialBlob serialBlob = (SerialBlob) undoValue.getValue();
                if (serialBlob != null) {
                    undoPST.setBlob(undoIndex, serialBlob.getBinaryStream());
                } else {
                    undoPST.setObject(undoIndex, null);
                }
            } else if (undoValue.getType() == JDBCType.CLOB.getVendorTypeNumber()) {
                SerialClob serialClob = (SerialClob) undoValue.getValue();
                if (serialClob != null) {
                    undoPST.setClob(undoIndex, serialClob.getCharacterStream());
                } else {
                    undoPST.setObject(undoIndex, null);
                }
            } else if (undoValue.getType() == JDBCType.OTHER.getVendorTypeNumber()) {
                undoPST.setObject(undoIndex, undoValue.getValue());
            } else {
                undoPST.setObject(undoIndex, undoValue.getValue(), undoValue.getType());
            }
        }
        // PK is at last one.
        // INSERT INTO a (x, y, z, pk) VALUES (?, ?, ?, ?)
        // UPDATE a SET x=?, y=?, z=? WHERE pk = ?
        // DELETE FROM a WHERE pk = ?
        undoIndex++;
        undoPST.setObject(undoIndex, pkValue.getValue(), pkValue.getType());
    }


    protected boolean dataValidationAndGoOn(Connection conn) throws SQLException {

        TableRecords beforeRecords = sqlUndoLog.getBeforeImage();
        TableRecords afterRecords = sqlUndoLog.getAfterImage();

        // Compare current data with before data
        // No need undo if the before data snapshot is equivalent to the after data snapshot.
        //对比快照的内容是否一致
//        Result<Boolean> beforeEqualsAfterResult = DataCompareUtils.isRecordsEquals(beforeRecords, afterRecords);
//        if (beforeEqualsAfterResult.getResult()) {
//            if (LOGGER.isInfoEnabled()) {
//                LOGGER.info("Stop rollback because there is no data change " +
//                        "between the before data snapshot and the after data snapshot.");
//            }
//            // no need continue undo.
//            return false;
//        }

        // Validate if data is dirty.
//        TableRecords currentRecords = queryCurrentRecords(conn);
//        // compare with current data and after image.
//        Result<Boolean> afterEqualsCurrentResult = DataCompareUtils.isRecordsEquals(afterRecords, currentRecords);
//        if (!afterEqualsCurrentResult.getResult()) {
//
//            // If current data is not equivalent to the after data, then compare the current data with the before
//            // data, too. No need continue to undo if current data is equivalent to the before data snapshot
//            Result<Boolean> beforeEqualsCurrentResult = DataCompareUtils.isRecordsEquals(beforeRecords, currentRecords);
//            if (beforeEqualsCurrentResult.getResult()) {
//                if (log.isInfoEnabled()) {
//                    log.info("Stop rollback because there is no data change " +
//                            "between the before data snapshot and the current data snapshot.");
//                }
//                // no need continue undo.
//                return false;
//            } else {
//                if (log.isInfoEnabled()) {
//                    if (StringUtils.isNotBlank(afterEqualsCurrentResult.getErrMsg())) {
//                        log.info(afterEqualsCurrentResult.getErrMsg(), afterEqualsCurrentResult.getErrMsgParams());
//                    }
//                }
//                if (log.isDebugEnabled()) {
//                    log.debug("check dirty datas failed, old and new data are not equal," +
//                            "tableName:[" + sqlUndoLog.getTableName() + "]," +
//                            "oldRows:[" + JSON.toJSONString(afterRecords.getRows()) + "]," +
//                            "newRows:[" + JSON.toJSONString(currentRecords.getRows()) + "].");
//                }
//                throw new SQLException("Has dirty records when undo.");
//            }
//        }
        return true;
    }
}
