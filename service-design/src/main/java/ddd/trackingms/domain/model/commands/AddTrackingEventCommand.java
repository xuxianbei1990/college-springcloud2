package ddd.trackingms.domain.model.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:40
 * Version:V1.0
 */
@Data
@AllArgsConstructor
public class AddTrackingEventCommand {
    private String bookingId;
    private Date eventTime;
    private String eventType;
    private String location;
    private String voyageNumber;

}
