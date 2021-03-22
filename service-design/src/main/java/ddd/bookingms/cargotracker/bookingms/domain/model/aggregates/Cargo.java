package ddd.bookingms.cargotracker.bookingms.domain.model.aggregates;

import ddd.bookingms.cargotracker.bookingms.domain.model.commands.BookCargoCommand;
import ddd.bookingms.cargotracker.bookingms.domain.model.entities.Location;
import ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

/**
 * 货物的聚合根
 *
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:24
 * Version:V1.0
 */
@Data
public class Cargo {


    private BookingId bookingId;

    private Location origin;

    private Delivery delivery;

    private CargoItinerary itinerary;

    private BookingAmount bookingAmount;

    private RouteSpecification routeSpecification;


    public Cargo() {

    }

    public Cargo(BookCargoCommand bookCargoCommand) {
        this.bookingId = new BookingId();
        this.bookingId.setBookingId(bookCargoCommand.getBookingId());
        this.routeSpecification = new RouteSpecification(
                new Location(bookCargoCommand.getOriginLocation()),
                new Location(bookCargoCommand.getDestLocation()),
                bookCargoCommand.getDestArrivalDeadline());
        this.origin = routeSpecification.getOrigin();
        this.itinerary = CargoItinerary.EMPTY_ITINERARY;
        this.bookingAmount = new BookingAmount(bookCargoCommand.getBookingAmount());

        this.delivery = Delivery.derivedFrom(this.routeSpecification,
                this.itinerary, LastCargoHandledEvent.EMPTY);
    }

}
