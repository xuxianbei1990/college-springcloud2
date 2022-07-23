package ddd.bookingms.cargotracker.infrastructure.respositories;

import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.bookingms.cargotracker.domain.model.aggregates.Cargo;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:31
 * Version:V1.0
 */
public interface CargoRepository {
    void save(Cargo cargo);

    Cargo findByBookingId(BookingId bookingId);
}
