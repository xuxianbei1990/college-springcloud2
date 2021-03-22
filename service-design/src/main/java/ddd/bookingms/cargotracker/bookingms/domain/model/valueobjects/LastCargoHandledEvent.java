package ddd.bookingms.cargotracker.bookingms.domain.model.valueobjects;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2021/3/22
 * Time: 10:02
 * Version:V1.0
 */
@Data
public class LastCargoHandledEvent {

    public static final LastCargoHandledEvent EMPTY = new LastCargoHandledEvent();

    private String handlingEventType;

    private String handlingEventLocation;

    private String handlingEventVoyage;
}
