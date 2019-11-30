/**
 * User: xuxianbei
 * Date: 2019/10/31
 * Time: 15:56
 * Version:V1.0
 * 设计模式原则：
 * 1.接口隔离；2：迪米特法则；3：单一责任原则；4：依赖倒置原则；5：优先组合；6：开闭原则
 */
public class SupplierPurchaseOrderImpl {

    //生成发货单
    public Integer generateDeliveryOrder() {
        //获取当前状态数据
        //校验数据
        //扣除批次库存(这里逻辑略复杂应该分离开写)
        //保存发货单(这里逻辑略复杂应该分离开写)
        return 0;
    }

    public Integer updateStatus() {
        /**
         * 因为主从原因导致数据查询不及时问题。
         * 个人觉得引入一个上下文的概念
         *
         * 如果现在系统架构是：
         * 单服务器：主从问题。这可以用上下文解决;
         * 多服务器：主从问题。还是加缓存锁吧。
         * 多服务器优化版：发消息，为什么不是直接调用？因为直接调用无法保证数据正确性。
         */
        return 0;
    }

    public Integer updateOrderStatus() {
        return 0;
    }

    //更新状态
    public Integer updateSupplierOrderStatus() {
        return 0;
    }
}
