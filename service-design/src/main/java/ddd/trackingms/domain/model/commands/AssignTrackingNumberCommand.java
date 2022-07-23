package ddd.trackingms.domain.model.commands;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:41
 * Version:V1.0
 */
@Data
public class AssignTrackingNumberCommand {

    private String bookingId;
    private String trackingNumber;

    public AssignTrackingNumberCommand() {
    }

    public AssignTrackingNumberCommand(String bookingId, String trackingNumber) {
        this.bookingId = bookingId;
        this.trackingNumber = trackingNumber;
    }
}
