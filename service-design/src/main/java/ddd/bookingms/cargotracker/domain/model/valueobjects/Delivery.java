package ddd.bookingms.cargotracker.domain.model.valueobjects;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import lombok.Data;

/**
 * 传送
 *
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 10:00
 * Version:V1.0
 */
@Data
public class Delivery {


    private LastCargoHandledEvent lastEvent;

    private RoutingStatus routingStatus;

    private TransportStatus transportStatus;

    private Voyage currentVoyage;

    /**
     * 目的地
     */
    private Location lastKnownLocation;

    public static Delivery derivedFrom(RouteSpecification routeSpecification,
                                       CargoItinerary itinerary, LastCargoHandledEvent lastCargoHandledEvent) {

        return new Delivery(lastCargoHandledEvent, itinerary, routeSpecification);
    }

    public Delivery(LastCargoHandledEvent lastEvent, CargoItinerary itinerary,
                    RouteSpecification routeSpecification) {
        this.lastEvent = lastEvent;

        this.routingStatus = calculateRoutingStatus(itinerary,
                routeSpecification);
        this.transportStatus = calculateTransportStatus();
        this.lastKnownLocation = calculateLastKnownLocation();
        this.currentVoyage = calculateCurrentVoyage();
        //this.nextExpectedActivity = calculateNextExpectedActivity(
        // routeSpecification, itinerary);
    }

    private Voyage calculateCurrentVoyage() {
        if (getTransportStatus().equals(TransportStatus.ONBOARD_CARRIER) && lastEvent != null) {
            return new Voyage(lastEvent.getHandlingEventVoyage());
        } else {
            return null;
        }
    }

    private TransportStatus calculateTransportStatus() {
        System.out.println("Transport Status for last event" + lastEvent.getHandlingEventType());
        if (lastEvent.getHandlingEventType() == null) {
            return TransportStatus.NOT_RECEIVED;
        }

        switch (lastEvent.getHandlingEventType()) {
            case "LOAD":
                return TransportStatus.ONBOARD_CARRIER;
            case "UNLOAD":
            case "RECEIVE":
            case "CUSTOMS":
                return TransportStatus.IN_PORT;
            case "CLAIM":
                return TransportStatus.CLAIMED;
            default:
                return TransportStatus.UNKNOWN;
        }
    }

    /**
     * Calculate Last known location
     * @return
     */
    private Location calculateLastKnownLocation() {
        if (lastEvent != null) {
            return new Location(lastEvent.getHandlingEventLocation());
        } else {
            return null;
        }
    }

    private RoutingStatus calculateRoutingStatus(CargoItinerary itinerary,
                                                 RouteSpecification routeSpecification) {
        if (itinerary == null || itinerary == CargoItinerary.EMPTY_ITINERARY) {
            return RoutingStatus.NOT_ROUTED;
        } else {
            return RoutingStatus.ROUTED;
        }
    }
}
