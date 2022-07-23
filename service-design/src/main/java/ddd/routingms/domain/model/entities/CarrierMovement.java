package ddd.routingms.domain.model.entities;

import ddd.bookingms.cargotracker.domain.model.entities.Location;
import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:08
 * Version:V1.0
 */
@Data
public class CarrierMovement {
    private Long id;

    private Date arrivalDate;

    private Date departureDate;

    private Location arrivalLocation;

    private Location departureLocation;
}

