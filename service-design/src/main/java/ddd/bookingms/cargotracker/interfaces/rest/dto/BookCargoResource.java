package ddd.bookingms.cargotracker.interfaces.rest.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 预定货物资源
 * @author: xuxianbei
 * Date: 2021/3/21
 * Time: 18:19
 * Version:V1.0
 * 这个可以理解为DTO
 */
@Data
public class BookCargoResource {

    private int bookingAmount;
    private String originLocation;
    private String destLocation;
    private LocalDate destArrivalDeadline;
}
