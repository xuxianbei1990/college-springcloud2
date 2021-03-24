package ddd.shareddomain.event;

import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/23
 * Time: 20:15
 * Version:V1.0
 */
@Data
public class CargoHandledEventData {
    private String bookingId;
    private String handlingType;
    private Date handlingCompletionTime;
    private String handlingLocation;
    private String voyageNumber;

    public CargoHandledEventData() {
    }

    public CargoHandledEventData(
            String bookingId,
            Date handlingCompletionTime,
            String handlingLocation,
            String handlingType,
            String voyageNumber
    ) {
        this.bookingId = bookingId;
        this.handlingCompletionTime = handlingCompletionTime;
        this.handlingLocation = handlingLocation;
        this.handlingType = handlingType;
        this.voyageNumber = voyageNumber;
    }
}
