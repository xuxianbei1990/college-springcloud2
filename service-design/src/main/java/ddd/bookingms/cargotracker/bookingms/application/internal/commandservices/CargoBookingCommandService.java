package ddd.bookingms.cargotracker.bookingms.application.internal.commandservices;

import ddd.bookingms.cargotracker.bookingms.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.bookingms.domain.model.aggregates.Cargo;
import ddd.bookingms.cargotracker.bookingms.domain.model.commands.BookCargoCommand;
import ddd.bookingms.cargotracker.bookingms.infrastructure.respositories.CargoRepository;
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
}
