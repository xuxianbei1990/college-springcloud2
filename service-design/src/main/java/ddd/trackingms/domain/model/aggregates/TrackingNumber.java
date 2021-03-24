package ddd.trackingms.domain.model.aggregates;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:34
 * Version:V1.0
 */
@Data
public class TrackingNumber {

    private String trackingNumber;

    public TrackingNumber() {
    }

    public TrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

}
