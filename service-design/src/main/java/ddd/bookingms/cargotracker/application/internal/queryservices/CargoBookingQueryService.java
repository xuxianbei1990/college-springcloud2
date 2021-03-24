package ddd.bookingms.cargotracker.application.internal.queryservices;

import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.domain.model.aggregates.Cargo;
import ddd.bookingms.cargotracker.infrastructure.respositories.CargoRepository;
import org.springframework.stereotype.Service;

/**
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 18:07
 * Version:V1.0
 */
@Service
public class CargoBookingQueryService {

    private CargoRepository cargoRepository;


    public Cargo find(BookingId bookingId){
        return cargoRepository.findByBookingId(bookingId);
    }
}
