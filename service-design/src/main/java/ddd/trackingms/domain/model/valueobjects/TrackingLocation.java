package ddd.trackingms.domain.model.valueobjects;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:47
 * Version:V1.0
 */
@Data
public class TrackingLocation {

    private String unLocCode;

    public TrackingLocation() {
    }

    public TrackingLocation(String unLocCode) {
        this.unLocCode = unLocCode;
    }
}
