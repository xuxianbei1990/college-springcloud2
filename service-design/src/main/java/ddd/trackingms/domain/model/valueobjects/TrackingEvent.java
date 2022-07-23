package ddd.trackingms.domain.model.valueobjects;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:46
 * Version:V1.0
 */
@Data
public class TrackingEvent {
    private Long id;
    private TrackingVoyageNumber trackingVoyageNumber;
    private TrackingLocation trackingLocation;
    private TrackingEventType trackingEventType;

    public TrackingEvent(
            TrackingVoyageNumber trackingVoyageNumber,
            TrackingLocation trackingLocation,
            TrackingEventType trackingEventType){
        this.trackingEventType = trackingEventType;
        this.trackingVoyageNumber = trackingVoyageNumber;
        this.trackingLocation = trackingLocation;
    }
}
