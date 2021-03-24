package ddd.handlingms.cargotracker.domain.model.valueobjects;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:51
 * Version:V1.0
 */
@Data
public class CargoBookingId {
    private String bookingId;
    public CargoBookingId(){}
    public CargoBookingId(String bookingId){this.bookingId = bookingId;}
}
