package ddd.bookingms.cargotracker.application.internal.commandservices;

import ddd.bookingms.cargotracker.application.internal.outboundservices.acl.ExternalCargoRoutingService;
import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.domain.model.aggregates.Cargo;
import ddd.bookingms.cargotracker.domain.model.commands.BookCargoCommand;
import ddd.bookingms.cargotracker.domain.model.commands.RouteCargoCommand;
import ddd.bookingms.cargotracker.domain.model.valueobjects.CargoItinerary;
import ddd.bookingms.cargotracker.infrastructure.respositories.CargoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:21
 * Version:V1.0
 */
@Service
public class CargoBookingCommandService {


    private CargoRepository cargoRepository;

    private ExternalCargoRoutingService externalCargoRoutingService;

    /**
     * 货物预定
     *
     * @param bookCargoCommand
     * @return
     */
    public BookingId bookCargo(BookCargoCommand bookCargoCommand) {
        String random = UUID.randomUUID().toString().toUpperCase();
        bookCargoCommand.setBookingId(random.substring(0, random.indexOf("-")));
        Cargo cargo = new Cargo(bookCargoCommand);
        cargoRepository.save(cargo);
        BookingId bookingId = new BookingId();
        bookingId.setBookingId(bookCargoCommand.getBookingId());
        return bookingId;
    }

    public void assignRouteToCargo(RouteCargoCommand routeCargoCommand) {
        System.out.println("Route Cargo command" + routeCargoCommand.getCargoBookingId());

        Cargo cargo = cargoRepository.findByBookingId(
                new BookingId(routeCargoCommand.getCargoBookingId()));

        //通过Http获取货物行程
        CargoItinerary cargoItinerary = externalCargoRoutingService.fetchRouteForSpecification(cargo.getRouteSpecification());

        cargo.assignToRoute(cargoItinerary);
        cargoRepository.save(cargo);
    }
}
