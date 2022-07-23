package ddd.trackingms.interfaces.events.transform;

import ddd.shareddomain.event.CargoHandledEvent;
import ddd.shareddomain.event.CargoHandledEventData;
import ddd.trackingms.domain.model.commands.AddTrackingEventCommand;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:43
 * Version:V1.0
 */
public class TrackingActivityCommandEventAssembler {

    public static AddTrackingEventCommand toCommandFromEvent(CargoHandledEvent cargoHandledEvent){
        CargoHandledEventData eventData = cargoHandledEvent.getCargoHandledEventData();
        return new AddTrackingEventCommand(
                eventData.getBookingId(),
                eventData.getHandlingCompletionTime(),
                eventData.getHandlingType(),
                eventData.getHandlingLocation(),
                eventData.getVoyageNumber());
    }
}
