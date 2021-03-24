package ddd.bookingms.cargotracker.domain.model.valueobjects;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import lombok.Data;

import java.time.LocalDate;

/**
 * 路由说明书
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
