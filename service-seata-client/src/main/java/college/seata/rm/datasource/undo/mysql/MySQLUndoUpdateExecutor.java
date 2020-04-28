package college.seata.rm.datasource.undo.mysql;

import college.seata.rm.datasource.sql.struct.TableRecords;
import college.seata.rm.datasource.undo.AbstractUndoExecutor;
import college.seata.rm.datasource.undo.SQLUndoLog;

/**
 * @author: xuxianbei
 * Date: 2020/3/31
 * Time: 10:53
 * Version:V1.0
 */
public class MySQLUndoUpdateExecutor extends AbstractUndoExecutor {
    public MySQLUndoUpdateExecutor(SQLUndoLog sqlUndoLog) {

    }

    @Override
    protected String buildUndoSQL() {
        return null;
    }

    @Override
    protected TableRecords getUndoRows() {
        return null;
    }
}
