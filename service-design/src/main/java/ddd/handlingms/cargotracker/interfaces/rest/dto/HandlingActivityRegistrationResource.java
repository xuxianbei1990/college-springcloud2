package ddd.handlingms.cargotracker.interfaces.rest.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:46
 * Version:V1.0
 */
@Data
public class HandlingActivityRegistrationResource {

    private String bookingId;
    private String voyageNumber;
    private String unLocode;
    private String handlingType;
    private LocalDate completionTime;
}
