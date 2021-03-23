package ddd.bookingms.cargotracker.bookingms.domain.model.aggregates;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:18
 * Version:V1.0
 */
@Data
public class BookingId {
    private String bookingId;

    public BookingId() {

    }

    public BookingId(String bookingId) {
        this.bookingId = bookingId;
    }

}
