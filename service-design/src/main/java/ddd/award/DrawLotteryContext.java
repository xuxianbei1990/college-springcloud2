package ddd.award;

import ddd.award.domain.MtCifyInfo;

/**
 * @author: xuxianbei
 * Date: 2020/6/19
 * Time: 17:52
 * Version:V1.0
 */
public class DrawLotteryContext {

    public MtCifyInfo getMtCityInfo() {
        return new MtCifyInfo();
    }

    public Integer getGameScore(){
        return  1;
    }
}
