package ddd.bookingms.cargotracker.bookingms.interfaces.rest;

import ddd.bookingms.cargotracker.bookingms.application.internal.commandservices.CargoBookingCommandService;
import ddd.bookingms.cargotracker.bookingms.application.internal.queryservices.CargoBookingQueryService;
import ddd.bookingms.cargotracker.bookingms.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.bookingms.domain.model.aggregates.Cargo;
import ddd.bookingms.cargotracker.bookingms.interfaces.rest.dto.BookCargoResource;
import ddd.bookingms.cargotracker.bookingms.interfaces.rest.transform.BookCargoCommandDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:15
 * Version:V1.0
 */
@RestController
@RequestMapping("/cargobooking")
public class CargoBookingController {

    @Autowired
    private CargoBookingCommandService cargoBookingCommandService;

    private CargoBookingQueryService cargoBookingQueryService;

    /**
     * 预定货物
     *
     * @param bookCargoResource
     * @return
     */
    @PostMapping
    public BookingId bookCargo(@RequestBody BookCargoResource bookCargoResource) {
        System.out.println("****Cargo Booked ****" + bookCargoResource.getBookingAmount());
        BookingId bookingId = cargoBookingCommandService.bookCargo(
                BookCargoCommandDTOAssembler.toCommandFromDTO(bookCargoResource));
        return bookingId;
    }


    /**
     * 查找货物
     *
     * @param bookingId
     * @return
     */
    @GetMapping("/findCargo")
    public Cargo findByBookingId(@RequestParam("bookingId") String bookingId) {
        return cargoBookingQueryService.find(new BookingId(bookingId));
    }
}
