package ddd.award.tranditional;

/**
 * 抽奖服务
 *
 * @author: xuxianbei
 * Date: 2020/6/19
 * Time: 9:55
 * Version:V1.0
 * 解决复杂和大规模软件的武器可以被粗略地归为三类：抽象、分治和知识
 * <p>
 * DDD的核心诉求就是将业务架构映射到系统架构上，在响应业务变化调整业务架构时，
 * 也随之变化系统架构。而微服务追求业务层面的复用，设计出来的系统架构和业务一致；
 * 在技术架构上则系统模块之间充分解耦，可以自由地选择合适的技术架构，去中心化地治理技术和数据。
 */
public class LotteryService {

    public Award drawLottery(Integer poolId) {
        /**
         * AwardPool awardPool = awardPoolDao.getAwardPool(poolId);//sql查询，将数据映射到AwardPool对象
         * for (Award award : awardPool.getAwards()) {
         *    //寻找到符合award.getProbability()概率的award
         * }
         */
        return null;
    }
}
