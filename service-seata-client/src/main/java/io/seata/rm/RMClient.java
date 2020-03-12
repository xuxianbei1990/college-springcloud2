package io.seata.rm;

import college.springcloud.io.seata.core.rpc.netty.RmMessageListener;
import college.springcloud.io.seata.core.rpc.netty.RmRpcClient;

/**
 * @author: xuxianbei
 * Date: 2020/3/6
 * Time: 15:29
 * Version:V1.0
 * 遗留问题：AbstractRMHandler  的  getResourceManager() 拿到对应的数据库管理器  这个不是重点
 * 重点是这个资源管理器做的事情。是我想知道的
 */
public class RMClient {

    public static void init(String applicationId, String transactionServiceGroup) {
        RmRpcClient rmRpcClient = RmRpcClient.getInstance(applicationId, transactionServiceGroup);
        //处理数据库相关
        rmRpcClient.setResourceManager(DefaultResourceManager.get());
        //为什么这里要加一个监听器？扩展？
        /**
         * RmMessageListener 负责消息监控，接收，分发；  DefaultRMHandler：负责消息处理
         */
        rmRpcClient.setClientMessageListener(new RmMessageListener(DefaultRMHandler.get()));
        rmRpcClient.init();
    }
}
