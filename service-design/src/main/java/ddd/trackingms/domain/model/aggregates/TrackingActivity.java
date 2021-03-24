package ddd.trackingms.domain.model.aggregates;

import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.trackingms.domain.model.commands.AddTrackingEventCommand;
import ddd.trackingms.domain.model.commands.AssignTrackingNumberCommand;
import ddd.trackingms.domain.model.entities.TrackingActivityEvent;
import ddd.trackingms.domain.model.valueobjects.TrackingEvent;
import ddd.trackingms.domain.model.valueobjects.TrackingEventType;
import ddd.trackingms.domain.model.valueobjects.TrackingLocation;
import ddd.trackingms.domain.model.valueobjects.TrackingVoyageNumber;
import lombok.Data;

/**
 * 追踪活动
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:35
 * Version:V1.0
 */
@Data
public class TrackingActivity {

    private TrackingActivityEvent trackingActivityEvent;

    private BookingId bookingId;

    private TrackingNumber trackingNumber;

    public void addTrackingEvent(AddTrackingEventCommand addTrackingEventCommand){
        TrackingEvent trackingEvent = new TrackingEvent(
                new TrackingVoyageNumber(addTrackingEventCommand.getVoyageNumber()),
                new TrackingLocation(addTrackingEventCommand.getLocation()),
                new TrackingEventType(addTrackingEventCommand.getEventType(),addTrackingEventCommand.getEventTime()));
        this.trackingActivityEvent.getTrackingEvents().add(trackingEvent);
    }

    public TrackingActivity(AssignTrackingNumberCommand assignTrackingNumberCommand){
        this.trackingNumber = new TrackingNumber(assignTrackingNumberCommand.getTrackingNumber());
        this.bookingId = new BookingId((assignTrackingNumberCommand.getBookingId()));
        this.trackingActivityEvent = TrackingActivityEvent.EMPTY_ACTIVITY;
    }
}
