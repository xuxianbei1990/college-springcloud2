package ddd.bookingms.cargotracker.domain.model.commands;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 9:41
 * Version:V1.0
 */
@Data
public class RouteCargoCommand {
    private String cargoBookingId;

    public RouteCargoCommand(String cargoBookingId) {
        this.setCargoBookingId(cargoBookingId);
    }

}
