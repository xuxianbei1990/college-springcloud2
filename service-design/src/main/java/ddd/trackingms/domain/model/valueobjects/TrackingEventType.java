package ddd.trackingms.domain.model.valueobjects;

import lombok.Data;

import java.util.Date;

/**
 * @author: xuxianbei
 * Date: 2021/3/24
 * Time: 17:48
 * Version:V1.0
 */
@Data
public class TrackingEventType {

    private String eventType;

    private Date eventTime;

    public TrackingEventType(){}
    public TrackingEventType(String eventType,Date eventTime){this.eventType = eventType;this.eventTime=eventTime;}

}
