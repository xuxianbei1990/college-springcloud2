package io.seata.tm;

import college.springcloud.io.seata.core.rpc.netty.TmRpcClient;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 15:28
 * Version:V1.0
 */
public class TMClient {

    public static void init(String applicaitonId, String transactionServiceGroup) {
        TmRpcClient tmRpcClient = TmRpcClient.getInstance(applicaitonId, transactionServiceGroup);
        tmRpcClient.init();
    }
}
