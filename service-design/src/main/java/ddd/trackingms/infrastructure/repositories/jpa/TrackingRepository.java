package ddd.trackingms.infrastructure.repositories.jpa;

import ddd.bookingms.cargotracker.domain.model.aggregates.BookingId;
import ddd.trackingms.domain.model.aggregates.TrackingActivity;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:32
 * Version:V1.0
 */
public interface TrackingRepository {

    TrackingActivity findByBookingNumber(BookingId bookingId);

    void save(TrackingActivity trackingActivity);
}
