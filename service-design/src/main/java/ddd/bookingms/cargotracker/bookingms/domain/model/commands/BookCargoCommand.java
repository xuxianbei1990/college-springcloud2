package ddd.bookingms.cargotracker.bookingms.domain.model.commands;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:23
 * Version:V1.0
 */
@Data
public class BookCargoCommand {

    private String bookingId;
    private int bookingAmount;
    private String originLocation;
    private String destLocation;
    private LocalDate destArrivalDeadline;
}
