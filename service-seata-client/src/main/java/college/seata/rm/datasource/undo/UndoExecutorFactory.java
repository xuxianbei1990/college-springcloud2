package college.seata.rm.datasource.undo;

import college.seata.rm.datasource.undo.mysql.MySQLUndoUpdateExecutor;
import college.springcloud.io.seata.core.exception.ShouldNeverHappenException;

/**
 * @author: xuxianbei
 * Date: 2020/3/31
 * Time: 10:58
 * Version:V1.0
 */
public class UndoExecutorFactory {

    public static AbstractUndoExecutor getUndoExecutor(String dbType, SQLUndoLog sqlUndoLog) {
        AbstractUndoExecutor result = null;
        switch (sqlUndoLog.getSqlType()) {
            case INSERT:
//                result = holder.getInsertExecutor(sqlUndoLog);
                break;
            case UPDATE:
                result = new MySQLUndoUpdateExecutor(sqlUndoLog);
                break;
            case DELETE:
//                result = holder.getDeleteExecutor(sqlUndoLog);
                break;
            default:
                throw new ShouldNeverHappenException();
        }
        return result;
    }
}
