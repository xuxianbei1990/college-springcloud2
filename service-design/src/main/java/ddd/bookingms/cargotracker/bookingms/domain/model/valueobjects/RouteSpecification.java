package ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects;

import ddd.bookingms.cargotracker.bookingms.domain.model.entities.Location;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 9:31
 * Version:V1.0
 */
@Data
public class RouteSpecification {

    private Location origin;

    private Location destination;

    private LocalDate arrivalDeadline;

    public RouteSpecification(Location origin, Location destination,
                              LocalDate arrivalDeadline) {
        this.origin = origin;
        this.destination = destination;
        this.arrivalDeadline = arrivalDeadline;
    }
}
