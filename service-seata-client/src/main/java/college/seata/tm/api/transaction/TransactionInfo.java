package college.seata.tm.api.transaction;

import lombok.Data;

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
}
