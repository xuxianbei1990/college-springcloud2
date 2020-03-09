package io.seata;

import io.seata.rm.RMClient;
import io.seata.tm.TMClient;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 15:34
 * Version:V1.0
 */
public class GlobalTransactionScanner {

    public static void main(String[] args) {
        TMClient.init("1", "2");
        RMClient.init("1", "2");
    }
}
