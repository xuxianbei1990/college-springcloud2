package ddd.award.domain;

import ddd.award.DrawLotteryContext;
import ddd.award.tranditional.AwardPool;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2020/6/19
 * Time: 17:48
 * Version:V1.0
 */
public class DrawLottery {
    //抽奖id
    private int lotteryId;
    //奖池列表
    private List<AwardPool> awardPools;

    public void setLotteryId(int lotteryId) {
        if (lotteryId <= 0) {
            throw new IllegalArgumentException("非法的抽奖id");
        }
        this.lotteryId = lotteryId;
    }

    //根据抽奖入参context选择奖池
    public AwardPool chooseAwardPool(DrawLotteryContext context) {
        if (context.getMtCityInfo() != null) {
            return chooseAwardPoolByCityInfo(awardPools, context.getMtCityInfo());
        } else {
            return chooseAwardPoolByScore(awardPools, context.getGameScore());
        }
    }

    private AwardPool chooseAwardPoolByCityInfo(List<AwardPool> awardPools, MtCifyInfo cityInfo) {
        return awardPools.stream().filter(t -> t.matchedCity(cityInfo.getCityId())).findFirst().get();
    }

    //根据抽奖活动得分选择奖池
    private AwardPool chooseAwardPoolByScore(List<AwardPool> awardPools, int gameScore) {//
        // }
        return null;
    }
}