package college.springcloud;

import college.springcloud.io.seata.core.rpc.netty.NettyServerConfig;
import college.springcloud.io.seata.core.rpc.netty.RpcServer;

/**
 * 假设你要做一个seata，首先你要解决哪些问题？
 * 第一问题？RPC通信问题
 * 第二问题？拦截sql操作，至少我要知道哪些是insert, select, update, 我想到的拦截是mybatis的拦截
 *
 * @author: xuxianbei
 * Date: 2020/1/19
 * Time: 16:28
 * Version:V1.0
 */
public class SeataServerApplication {

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer(new NettyServerConfig());
        rpcServer.setListenPort(8091);
        rpcServer.init();
    }
}
