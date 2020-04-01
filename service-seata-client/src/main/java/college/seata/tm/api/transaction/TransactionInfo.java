package college.seata.tm.api.transaction;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Set;

/**
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:20
 * Version:V1.0
 */
@Data
public class TransactionInfo {

    private int timeOut;

    private String name;

    private Set<RollbackRule> rollbackRules;

    public boolean rollbackOn(Throwable ex) {

        RollbackRule winner = null;
        int deepest = Integer.MAX_VALUE;

        if (CollectionUtils.isNotEmpty(rollbackRules)) {
            winner = NoRollbackRule.DEFAULT_NO_ROLLBACK_RULE;
            for (RollbackRule rule : this.rollbackRules) {
                int depth = rule.getDepth(ex);
                if (depth >= 0 && depth < deepest) {
                    deepest = depth;
                    winner = rule;
                }
            }
        }

        return winner == null || !(winner instanceof NoRollbackRule);
    }
}
