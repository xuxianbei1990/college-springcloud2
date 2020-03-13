package college.seata.tm.api.transaction;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:32
 * Version:V1.0
 */
public class NoRollbackRule extends RollbackRule {

    public NoRollbackRule(Class<?> clazz) {
        super(clazz);
    }


    public NoRollbackRule(String exceptionName) {
        super(exceptionName);
    }
}
