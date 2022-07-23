package ddd.handlingms.cargotracker.domain.model.commands;

import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 19:47
 * Version:V1.0
 */
@Data
public class HandlingActivityRegistrationCommand {
    private Date completionTime;
    private String bookingId;
    private String voyageNumber;
    private String unLocode;
    private String handlingType;

    public HandlingActivityRegistrationCommand(String bookingId, String voyageNumber, String unLocode, String handlingType, Date completionTime){
        this.setCompletionTime(completionTime);
        this.setBookingId(bookingId);
        this.setVoyageNumber(voyageNumber);
        this.setUnLocode(unLocode);
        this.setHandlingType(handlingType);
    }
}
