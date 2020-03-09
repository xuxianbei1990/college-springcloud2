package io.seata.rm;

import college.springcloud.io.seata.core.rpc.netty.RmMessageListener;
import college.springcloud.io.seata.core.rpc.netty.RmRpcClient;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 15:29
 * Version:V1.0
 */
public class RMClient {

    public static void init(String applicationId, String transactionServiceGroup) {
        RmRpcClient rmRpcClient = RmRpcClient.getInstance(applicationId, transactionServiceGroup);
        //处理数据库相关
        rmRpcClient.setResourceManager(DefaultResourceManager.get());
        //为什么这里要加一个监听器？扩展？
        rmRpcClient.setClientMessageListener(new RmMessageListener(DefaultRMHandler.get()));
        rmRpcClient.init();
    }
}
