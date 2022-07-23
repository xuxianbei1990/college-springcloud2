package ddd.bookingms.cargotracker.interfaces.rest;

import ddd.bookingms.cargotracker.application.internal.commandservices.CargoBookingCommandService;
import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.interfaces.rest.dto.RouteCargoResource;
import ddd.bookingms.cargotracker.interfaces.rest.transform.RouteCargoCommandDTOAssembler;
import org.springframework.web.bind.annotation.*;

/**
 * 货物路由
 *
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 18:12
 * Version:V1.0
 */
@RestController
@RequestMapping("/cargorouting")
public class CargoRoutingController {


    private CargoBookingCommandService cargoBookingCommandService;

    @PostMapping
    @ResponseBody
    public BookingId routeCargo(@RequestBody RouteCargoResource routeCargoResource) {
        cargoBookingCommandService.assignRouteToCargo(RouteCargoCommandDTOAssembler.toCommandFromDTO(routeCargoResource));

        BookingId bookingId = new BookingId(routeCargoResource.getBookingId());
        return bookingId;
    }
}
