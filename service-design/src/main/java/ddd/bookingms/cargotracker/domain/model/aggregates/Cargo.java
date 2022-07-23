package ddd.bookingms.cargotracker.domain.model.aggregates;

import ddd.bookingms.cargotracker.domain.model.commands.BookCargoCommand;
import ddd.bookingms.cargotracker.domain.model.entities.Location;
import ddd.bookingms.cargotracker.domain.model.valueobjects.*;
import ddd.shareddomain.event.CargoBookedEvent;
import ddd.shareddomain.event.CargoBookedEventData;
import ddd.shareddomain.event.CargoRoutedEvent;
import ddd.shareddomain.event.CargoRoutedEventData;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * 货物的聚合根
 * 这里无法存储。因为mybatis无法直接使用。
 *
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:24
 * Version:V1.0
 */
@Data
public class Cargo extends AbstractAggregateRoot<Cargo> {

    private Long id;

    /**
     * 预定信息
     */
    private BookingId bookingId;

    /**
     * 出发地
     */
    private Location origin;

    /**
     * 传送
     */
    private Delivery delivery;

    /**
     * 货物旅程
     */
    private CargoItinerary itinerary;

    /**
     * 预定金额
     */
    private BookingAmount bookingAmount;

    /**
     * 路由说明
     */
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
        addDomainEvent(new CargoBookedEvent(new CargoBookedEventData(this.bookingId.getBookingId())));
    }

    public void assignToRoute(CargoItinerary cargoItinerary) {
        this.itinerary = cargoItinerary;
        addDomainEvent(new CargoRoutedEvent(new CargoRoutedEventData(this.bookingId.getBookingId())));
    }

    public void addDomainEvent(Object event) {
        registerEvent(event);
    }
}
