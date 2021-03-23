package ddd.bookingms.cargotracker.bookingms.interfaces.rest.transform;

import ddd.bookingms.cargotracker.bookingms.domain.model.commands.RouteCargoCommand;
import ddd.bookingms.cargotracker.bookingms.interfaces.rest.dto.RouteCargoResource;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:27
 * Version:V1.0
 */
public class RouteCargoCommandDTOAssembler {

    public static RouteCargoCommand toCommandFromDTO(RouteCargoResource routeCargoResource){

        return new RouteCargoCommand(routeCargoResource.getBookingId());
    }
}
