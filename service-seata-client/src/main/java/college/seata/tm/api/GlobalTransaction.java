package college.seata.tm.api;

import college.springcloud.io.seata.core.exception.TransactionException;

/**
 * 提供一个事务回话全套操作
 *
 * @author: xuxianbei
 * Date: 2020/3/12
 * Time: 17:39
 * Version:V1.0
 */
public interface GlobalTransaction {

    void begin(int timeout, String name) throws TransactionException;

    void commit() throws TransactionException;
}
