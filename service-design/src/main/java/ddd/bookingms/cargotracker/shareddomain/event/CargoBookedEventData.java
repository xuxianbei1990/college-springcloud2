package ddd.bookingms.cargotracker.shareddomain.event;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 10:43
 * Version:V1.0
 */
@Data
public class CargoBookedEventData {
    private String bookingId;

    public CargoBookedEventData(){}
    public CargoBookedEventData(String bookingId){ this.bookingId = bookingId; }
}
