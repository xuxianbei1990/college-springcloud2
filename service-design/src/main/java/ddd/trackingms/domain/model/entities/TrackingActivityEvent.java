package ddd.trackingms.domain.model.entities;

import ddd.trackingms.domain.model.valueobjects.TrackingEvent;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:51
 * Version:V1.0
 */
@Data
public class TrackingActivityEvent {

    public static final TrackingActivityEvent EMPTY_ACTIVITY = new TrackingActivityEvent();

    private List<TrackingEvent> trackingEvents = new ArrayList<TrackingEvent>();

}
