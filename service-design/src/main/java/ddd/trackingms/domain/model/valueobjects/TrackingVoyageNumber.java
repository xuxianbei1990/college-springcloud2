package ddd.trackingms.domain.model.valueobjects;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:47
 * Version:V1.0
 */
@Data
public class TrackingVoyageNumber {

    private String voyageNumber;

    public TrackingVoyageNumber(){}
    public TrackingVoyageNumber(String voyageNumber){this.voyageNumber = voyageNumber;}

}
