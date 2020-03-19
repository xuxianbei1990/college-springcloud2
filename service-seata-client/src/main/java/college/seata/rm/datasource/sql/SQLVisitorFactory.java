package college.seata.rm.datasource.sql;

import college.seata.rm.datasource.sql.mysql.MySqlOperateRecognizerHolder;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/3/19
 * Time: 20:56
 * Version:V1.0
 */
public class SQLVisitorFactory {

    public static SQLRecognizer get(String sql, String dbType) {
        List<SQLStatement> asts = SQLUtils.parseStatements(sql, dbType);
        System.out.println(asts);
        SQLRecognizer recognizer = null;
        SQLStatement ast = asts.get(0);
        SQLOperateRecognizerHolder recognizerHolder = new MySqlOperateRecognizerHolder();
        if (ast instanceof SQLInsertStatement) {
            recognizer = recognizerHolder.getInsertRecognizer(sql, ast);
        } else if (ast instanceof SQLUpdateStatement) {
            recognizer = recognizerHolder.getUpdateRecognizer(sql, ast);
        } else if (ast instanceof SQLDeleteStatement) {
            recognizer = recognizerHolder.getDeleteRecognizer(sql, ast);
        } else if (ast instanceof SQLSelectStatement) {
            recognizer = recognizerHolder.getSelectForUpdateRecognizer(sql, ast);
        }
        return recognizer;
    }

    public static void main(String[] args) {

        System.out.println(get("select * from xxb where dema = 1", "MYSQL"));
    }
}
