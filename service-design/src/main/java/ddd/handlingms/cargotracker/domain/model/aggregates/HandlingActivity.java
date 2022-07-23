package ddd.handlingms.cargotracker.domain.model.aggregates;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import ddd.handlingms.cargotracker.domain.model.valueobjects.CargoBookingId;
import ddd.handlingms.cargotracker.domain.model.valueobjects.Type;
import ddd.handlingms.cargotracker.domain.model.valueobjects.VoyageNumber;
import ddd.shareddomain.event.CargoHandledEvent;
import ddd.shareddomain.event.CargoHandledEventData;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:50
 * Version:V1.0
 */
@Data
public class HandlingActivity extends AbstractAggregateRoot<HandlingActivity> {

    private VoyageNumber voyageNumber;

    private Date completionTime;

    private Type type;

    private Location location;

    private CargoBookingId cargoBookingId;

    public HandlingActivity(CargoBookingId cargoBookingId, Date completionTime,
                            Type type, Location location, VoyageNumber voyageNumber) {
        if (type.prohibitsVoyage()) {
            throw new IllegalArgumentException(
                    "VoyageNumber is not allowed with event type " + type);
        }

        this.voyageNumber = voyageNumber;
        this.completionTime = (Date) completionTime.clone();
        this.type = type;
        this.location = location;
        this.cargoBookingId = cargoBookingId;

        CargoHandledEvent cargoHandledEvent =
                new CargoHandledEvent(
                        new CargoHandledEventData(
                                this.cargoBookingId.getBookingId(),
                                this.completionTime,
                                this.location.getUnLocCode(),
                                this.type.toString(),
                                this.voyageNumber.getVoyageNumber()));


        //Add this domain event which needs to be fired when the new cargo is saved
        addDomainEvent(cargoHandledEvent);
    }

    public void addDomainEvent(Object event) {
        registerEvent(event);
    }
}
